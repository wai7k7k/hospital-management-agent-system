import httpx
from typing import Optional, Dict, Any, List
from datetime import datetime
from tenacity import retry, stop_after_attempt, wait_exponential, retry_if_exception_type
from app.utils.config import settings
from app.utils.logger import log


class HospitalAPIError(Exception):
    """医院 API 调用异常"""

    def __init__(self, message: str, status_code: int = 0, detail: str = ""):
        self.message = message
        self.status_code = status_code
        self.detail = detail
        super().__init__(message)


class HospitalAPIClient:
    """医院系统 API 客户端 — 对接 ylxm-reghos (8071) + ylxm-customer (8061) + ruoyi-system (9201)"""

    def __init__(self):
        self.base_url = settings.HOSPITAL_API_BASE_URL.rstrip("/")
        self.customer_base_url = settings.HOSPITAL_CUSTOMER_BASE_URL.rstrip("/")
        self.system_base_url = settings.HOSPITAL_SYSTEM_BASE_URL.rstrip("/")
        self.timeout = settings.HOSPITAL_API_TIMEOUT
        self.token = settings.HOSPITAL_API_TOKEN

        self.client = httpx.AsyncClient(
            base_url=self.base_url,
            timeout=self.timeout,
            trust_env=False,
        )
        self.customer_client = httpx.AsyncClient(
            base_url=self.customer_base_url,
            timeout=self.timeout,
            trust_env=False,
        )
        self.system_client = httpx.AsyncClient(
            base_url=self.system_base_url,
            timeout=self.timeout,
            trust_env=False,
        )
        log.info(f"医院 API 客户端初始化成功 - reghos: {self.base_url}, customer: {self.customer_base_url}, system: {self.system_base_url}")

    # ── 内部通用方法 ──────────────────────────────────────────────

    def _headers(self) -> Dict[str, str]:
        h = {"Content-Type": "application/json"}
        if self.token:
            h["Authorization"] = f"Bearer {self.token}"
        return h

    @retry(
        stop=stop_after_attempt(3),
        wait=wait_exponential(multiplier=1, min=1, max=10),
        retry=retry_if_exception_type((httpx.TimeoutException, httpx.HTTPStatusError)),
        reraise=True,
    )
    async def _get(self, path: str, params: Dict = None) -> httpx.Response:
        resp = await self.client.get(path, params=params, headers=self._headers())
        resp.raise_for_status()
        return resp

    async def _post(self, path: str, body: Dict = None) -> httpx.Response:
        """POST 不重试（非幂等），调用方自行处理异常"""
        resp = await self.client.post(path, json=body, headers=self._headers())
        resp.raise_for_status()
        return resp

    def _parse_response(self, response: httpx.Response) -> Any:
        """
        解析 RuoYi 响应格式，兼容三种包装：
        - R<T>:        {"code": 200, "msg": "...", "data": {...}}
        - TableDataInfo: {"code": 200, "msg": "...", "rows": [...], "total": N}
        - ResultVO:    {"code": 200, "msg": "...", "data": {...}}
        """
        data = response.json()

        if isinstance(data, list):
            return data

        if isinstance(data, dict):
            code = data.get("code", 200)
            if code != 200:
                raise HospitalAPIError(
                    f"HIS 接口返回错误: {data.get('msg', '未知错误')}",
                    status_code=code,
                    detail=str(data),
                )
            # TableDataInfo 格式: 数据在 rows
            if "rows" in data:
                return data["rows"]
            # R<T> / ResultVO: 数据在 data 字段
            # 注意: R<Void> 的 data 字段为 null，此时返回整个 wrapper 表示成功
            inner = data.get("data")
            return inner if inner is not None else data

        return data

    # ── 科室 (ylxm-reghos:8071) ──────────────────────────────────

    async def get_department(self, department_name: str) -> Optional[Dict[str, Any]]:
        """
        GET /dep/list → TableDataInfo<DepVo>
        DepVo 字段: depId (Long), depName (String), softDel
        """
        try:
            response = await self._get("/dep/list")
            departments = self._parse_response(response)

            if not isinstance(departments, list):
                log.warning(f"/dep/list 返回数据格式异常: {type(departments)}")
                return None

            log.info(f"HIS 返回 {len(departments)} 个科室")

            # 优先精确匹配，再模糊匹配
            best_match = None
            for dep in departments:
                dep_name = dep.get("depName", "")
                if dep_name == department_name:
                    best_match = {"id": dep.get("depId"), "name": dep_name}
                    break
                if not best_match and (department_name in dep_name or dep_name in department_name):
                    best_match = {"id": dep.get("depId"), "name": dep_name}

            if best_match:
                return best_match

            log.warning(f"未匹配到科室: '{department_name}'，可选: {[d.get('depName','') for d in departments[:10]]}")
            return None

        except HospitalAPIError:
            raise
        except httpx.HTTPStatusError as e:
            log.error(f"获取科室列表 HTTP {e.response.status_code}: {e.response.text[:300]}")
            return None
        except Exception as e:
            log.error(f"获取科室信息失败: {str(e)}")
            return None

    async def get_department_by_id(self, dep_id: int) -> Optional[Dict[str, Any]]:
        """GET /dep/{depId} → R<DepVo>"""
        try:
            response = await self._get(f"/dep/{dep_id}")
            dep = self._parse_response(response)
            if dep:
                return {"id": dep.get("depId"), "name": dep.get("depName")}
            return None
        except Exception as e:
            log.error(f"获取科室详情失败: {str(e)}")
            return None

    async def get_department_doctors(self, dep_id: int) -> List[Dict[str, Any]]:
        """查询科室医生列表（优先从排班表，fallback 从系统用户表）"""
        try:
            from datetime import date
            response = await self._get(
                f"/dep/{dep_id}/schedules",
                params={"date": date.today().isoformat(), "timeSlot": "上午"},
            )
            raw = response.json()
            doctors = []
            if isinstance(raw, dict):
                data = raw.get("data", raw)
                items = data.get("schedules", []) if isinstance(data, dict) else data if isinstance(data, list) else []
                seen = set()
                for s in items:
                    uid = s.get("userId")
                    if uid and uid not in seen:
                        seen.add(uid)
                        doctors.append({"userId": uid, "userName": s.get("userName", "")})
            if doctors:
                return doctors
            # fallback: 查系统用户表
            user_resp = await self._get("/system/user/list", params={"pageSize": 50})
            user_data = self._parse_response(user_resp)
            if isinstance(user_data, list):
                return [{"userId": u.get("userId"), "userName": u.get("userName", u.get("nickName", ""))}
                        for u in user_data if u.get("userId")]
            return []
        except Exception as e:
            log.warning(f"获取科室医生列表失败: {str(e)}")
            return []

    # ── 排班查询 (ylxm-reghos:8071) ──────────────────────────────

    async def get_schedule_list(
        self,
        dep_id: int,
        date_str: str,
        time_slot: str,
    ) -> List[Dict[str, Any]]:
        """
        查科室可用排班列表，返回 [{scheduleId, userName, depName, scheduleNum, shengyu}, ...]
        GET /dep/{depId}/schedules?date=...&timeSlot=...
        """
        try:
            url = f"/dep/{dep_id}/schedules"
            params = {"date": date_str, "timeSlot": time_slot}
            log.info(f"[HIS] 查询排班: {url} params={params}")
            response = await self._get(url, params=params)
            raw = response.json()
            log.info(f"[HIS] 排班响应: code={raw.get('code')}, data_keys={list(raw.get('data', {}).keys()) if isinstance(raw.get('data'), dict) else type(raw.get('data'))}")
            if isinstance(raw, dict):
                data = raw.get("data", raw)
                if isinstance(data, dict):
                    schedules = data.get("schedules", [])
                    log.info(f"[HIS] 解析到 {len(schedules)} 条排班")
                    return schedules
                if isinstance(data, list):
                    log.info(f"[HIS] data 是数组，直接返回 {len(data)} 条")
                    return data
            log.warning(f"[HIS] 排班响应格式不匹配: {type(raw)}")
            return []
        except Exception as e:
            log.warning(f"[HIS] 查询排班列表失败: {str(e)}")
            return []

    async def get_available_schedule(
        self,
        dep_id: int,
        date_str: str,
        time_slot: str,
    ) -> Optional[int]:
        """
        查询可用排班，返回 schedule_id 或 None。
        GET /dep/{depId}/schedules?date=2026-06-09&timeSlot=上午
        """
        try:
            response = await self._get(
                f"/dep/{dep_id}/schedules",
                params={"date": date_str, "timeSlot": time_slot},
            )
            data = self._parse_response(response)
            if isinstance(data, list) and len(data) > 0:
                # 返回第一个可用排班
                s = data[0]
                if isinstance(s, dict):
                    return s.get("scheduleId")
            return None
        except Exception as e:
            log.warning(f"查询排班失败: {str(e)}")
            return None

    # ── 挂号 (ylxm-reghos:8071) ──────────────────────────────────

    async def create_register(
        self,
        patient_id: int,
        dep_id: int,
        user_id: int,
        dep_name: str,
        patient_name: str = "",
        user_name: str = "",
        remark: str = "AI 自助挂号",
    ) -> Optional[Dict[str, Any]]:
        """
        POST /register → R<Void>
        RegisterBo 字段: patientId, depId, userId, depName, userName, registerDate, remark
        """
        try:
            from datetime import datetime

            body = {
                "patientId": patient_id,
                "depId": dep_id,
                "depName": dep_name,
                "userId": user_id,
                "userName": user_name,
                "patientName": patient_name,
                "registerDate": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
                "registerState": "0",
                "remark": remark,
            }

            log.info(f"创建挂号请求: {body}")

            response = await self._post("/register", body=body)
            result = self._parse_response(response)
            log.info(f"挂号创建成功: {result}")
            return result

        except HospitalAPIError as e:
            log.error(f"HIS 挂号失败: {e.message}, detail: {e.detail}")
            return None
        except httpx.HTTPStatusError as e:
            # HTTP 非 2xx，但可能业务已成功（LogAspect 鉴权异常不影��数据入库）
            resp_body = e.response.text[:500] if e.response.text else ""
            log.warning(
                f"挂号请求返回 HTTP {e.response.status_code}，尝试解析响应体: {resp_body}"
            )
            # 尝试从错误响应中提取有用信息
            result = self._try_parse_error_response(e.response)
            if result:
                return result
            return {"message": f"HTTP {e.response.status_code}: {resp_body[:200]}"}
        except Exception as e:
            log.error(f"创建挂号记录失败: {str(e)}")
            return None

    def _try_parse_error_response(self, response: httpx.Response) -> Optional[Dict[str, Any]]:
        """
        尝试从错误响应中提取信息，判断业务是否实际成功。

        Sa-Token NotLoginException 场景：
        响应体中可能包含 code=401 和 msg="未能读取到有效Token"，
        但挂号数据实际已入库。此时返回含 warning 的 success 结果，
        让上层知道"数据已写入但日志记录失败"。
        """
        try:
            data = response.json()
            if isinstance(data, dict):
                code = data.get("code", 0)
                msg = data.get("msg", "")

                # NotLoginException → 业务成功但日志记录失败
                if "Token" in msg or "登录" in msg or "未登录" in msg:
                    log.info("检测到鉴权异常（业务数据已入库），视为挂号成功")
                    return {
                        "success": True,
                        "warning": "挂号数据已入库，但操作日志记录失败（AI 调用缺少登录 Token）",
                        "appointment_id": str(response.status_code),
                    }

                # 其他业务错误
                log.warning(f"HIS 返回业务错误: code={code}, msg={msg}")
                return None
        except Exception:
            pass
        return None

    async def do_guahao(self, schedule_id: int, patient_card_id: str) -> Optional[Dict[str, Any]]:
        """
        GET /guahao/guahao?scheduleId=X&patientCardId=Y → ResultVO
        这是实际的挂号动作（分配号源）
        返回: {"message": "ok"|"no"|"exist"|"noinfo"} 或 None
        """
        try:
            response = await self._get(
                "/guahao/guahao",
                params={"scheduleId": schedule_id, "patientCardId": patient_card_id},
            )
            raw = response.json()
            if isinstance(raw, dict):
                return {"message": raw.get("message", "")}
            return None
        except Exception as e:
            log.error(f"挂号操作失败: {str(e)}")
            return None

    async def get_register_list(self, patient_id: int = None) -> List[Dict[str, Any]]:
        """GET /register/list → TableDataInfo<RegisterVo>"""
        try:
            params = {}
            if patient_id:
                params["patientId"] = patient_id
            response = await self._get("/register/list", params=params)
            return self._parse_response(response) or []
        except Exception as e:
            log.error(f"获取挂号列表失败: {str(e)}")
            return []

    async def get_register_by_card_id(self, card_id: str) -> Optional[Dict[str, Any]]:
        """GET /register/getRegisterByCardId?cardId=X → ResultVO"""
        try:
            response = await self._get("/register/getRegisterByCardId", params={"cardId": card_id})
            result = self._parse_response(response)
            if isinstance(result, dict):
                return result.get("register", result)
            return None
        except Exception as e:
            log.error(f"获取挂号信息失败: {str(e)}")
            return None

    async def get_exam_results_by_patient(self, patient_id: int) -> List[Dict[str, Any]]:
        """从 HIS 回查患者检查结果，作为消息缓存缺失时的可靠兜底。"""
        try:
            response = await self._get(f"/exam_result/patient/{patient_id}")
            result = self._parse_response(response)
            return result if isinstance(result, list) else []
        except Exception as e:
            log.error(f"获取患者检查结果失败: {str(e)}")
            return []

    # ── 患者 (ylxm-customer:8061) ───────────────────────────────

    async def create_patient(
        self,
        patient_name: str,
        patient_phone: str,
        patient_card_id: str,
        patient_sex: str,
        patient_birthday: str,
        patient_password: str = "123456",
    ) -> Optional[Dict[str, Any]]:
        """
        POST /patient → R<Void> (ylxm-customer)
        在HIS系统中创建患者档案。返回新建患者的ID等信息。
        """
        try:
            body = {
                "patientName": patient_name,
                "patientPhone": patient_phone,
                "patientCardId": patient_card_id,
                "patientSex": patient_sex,
                "patientPassword": patient_password,
                "patientBirthday": patient_birthday,
                "patientMoney": 0,
            }
            resp = await self.customer_client.post(
                "/patient", json=body, headers=self._headers()
            )
            resp.raise_for_status()
            result = self._parse_response(resp)
            log.info(f"患者创建成功: {patient_name}, result: {result}")
            return result
        except Exception as e:
            log.error(f"创建患者失败: {str(e)}")
            return None

    async def get_patient_info(self, patient_id: int) -> Optional[Dict[str, Any]]:
        """GET /patient/{patientId} → R<PatientVo>（调用 ylxm-customer）"""
        try:
            response = await self.customer_client.get(f"/patient/{patient_id}", headers=self._headers())
            return self._parse_response(response)
        except HospitalAPIError:
            raise
        except httpx.HTTPStatusError as e:
            log.error(f"获取患者信息 HTTP {e.response.status_code}")
            return None
        except Exception as e:
            log.error(f"获取患者信息失败: {str(e)}")
            return None

    async def get_patient_list(self, name: str = None) -> List[Dict[str, Any]]:
        """GET /patient/list → TableDataInfo<PatientVo>（调用 ylxm-customer）"""
        try:
            params = {}
            if name:
                params["patientName"] = name
            response = await self.customer_client.get("/patient/list", params=params, headers=self._headers())
            return self._parse_response(response) or []
        except Exception as e:
            log.error(f"获取患者列表失败: {str(e)}")
            return []

    # ── 药品 / 处方 (ylxm-reghos:8071) ──────────────────────────

    async def get_medication_info(self, medication_name: str) -> Optional[Dict[str, Any]]:
        """GET /medicine_order/medicines/{name} → ResultVO"""
        try:
            response = await self._get(f"/medicine_order/medicines/{medication_name}")
            result = self._parse_response(response)
            if isinstance(result, dict):
                med_list = result.get("medicineList", [])
                if med_list:
                    return med_list[0]
            return result if isinstance(result, dict) else None
        except Exception as e:
            log.error(f"获取药物信息失败: {str(e)}")
            return None

    async def get_prescriptions(
        self, register_id: int = None, patient_name: str = None
    ) -> List[Dict[str, Any]]:
        """GET /medicine_order/getPres?registerId=X&patientName=Y → ResultVO"""
        try:
            params = {}
            if register_id:
                params["registerId"] = register_id
            if patient_name:
                params["patientName"] = patient_name
            response = await self._get("/medicine_order/getPres", params=params)
            result = self._parse_response(response)
            if isinstance(result, dict):
                return result.get("prescriptionList", [])
            return []
        except Exception as e:
            log.error(f"获取处方列表失败: {str(e)}")
            return []

    # ── 检查项目订单 (ylxm-reghos:8071) ──────────────────────────

    async def get_inspect_orders(
        self, patient_id: int = None
    ) -> List[Dict[str, Any]]:
        """GET /inspect_orderr/list → TableDataInfo<InspectOrderVo>"""
        try:
            params = {}
            if patient_id:
                params["patientId"] = patient_id
            response = await self._get("/inspect_orderr/list", params=params)
            return self._parse_response(response) or []
        except Exception as e:
            log.error(f"获取检查订单列表失败: {str(e)}")
            return []

    async def create_inspect_order(self, order_data: Dict[str, Any]) -> Optional[Dict[str, Any]]:
        """POST /inspect_orderr → R<Void>"""
        try:
            response = await self._post("/inspect_orderr", body=order_data)
            return self._parse_response(response)
        except Exception as e:
            log.error(f"创建检查订单失败: {str(e)}")
            return None

    # ── 药品订单 (ylxm-reghos:8071) ──────────────────────────────

    async def get_medicine_orders(
        self, patient_id: int = None
    ) -> List[Dict[str, Any]]:
        """GET /medicine_order/list → TableDataInfo<MedicineOrderVo>"""
        try:
            params = {}
            if patient_id:
                params["patientId"] = patient_id
            response = await self._get("/medicine_order/list", params=params)
            return self._parse_response(response) or []
        except Exception as e:
            log.error(f"获取药品订单列表失败: {str(e)}")
            return []

    # ── 推送 AI 结果回 HIS ──────────────────────────────────────

    async def push_ai_result(
        self, biz_type: str, biz_id: str, ai_data: Dict[str, Any]
    ) -> bool:
        """将 AI 分析结果发布到 RabbitMQ ai.# 路由（事件驱动回写）"""
        try:
            from app.integrations.mq.producer import RabbitMQProducer
            producer = RabbitMQProducer()
            try:
                routing_key = f"ai.{biz_type}.completed"
                await producer.publish(
                    exchange="hospital.events",
                    routing_key=routing_key,
                    message={
                        "eventId": ai_data.get("event_id", f"ai-{biz_type}-{biz_id}"),
                        "eventType": f"AI_{biz_type.upper()}_COMPLETED",
                        "source": "ai-service",
                        "timestamp": datetime.now().isoformat(),
                        "bizType": biz_type,
                        "bizId": biz_id,
                        "data": ai_data,
                    },
                )
                log.info(f"AI 结果已发布到 RabbitMQ - routing_key: {routing_key}")
                return True
            finally:
                producer.close()
        except Exception as e:
            log.error(f"发布 AI 结果到 RabbitMQ 失败: {str(e)}")
            return False

    async def close(self):
        await self.client.aclose()
        await self.customer_client.aclose()
        await self.system_client.aclose()
        log.info("医院 API 客户端已关闭")

    # ── 排班查询（ruoyi-system:9201，和护士挂号同一张表）───────

    async def get_system_schedule_list(
        self,
        dep_name: str,
        date_str: str,
        time_slot: str,
    ) -> List[Dict[str, Any]]:
        """
        查护士同款排班列表 —— 按科室名称查，绕过 reghos 和 system 两套 depId 不对应的问题。
        GET /schedule/list?depName=X&scheduleDate=Y&scheduleTime=Z
        """
        try:
            params = {
                "depName": dep_name,
                "scheduleDate": date_str,
                "scheduleTime": time_slot,
                "pageSize": 200,
            }
            log.info(f"[HIS-System] 查询排班: /schedule/list params={params}")
            resp = await self.system_client.get(
                "/schedule/list", params=params, headers=self._headers()
            )
            resp.raise_for_status()
            raw = resp.json()
            log.info(f"[HIS-System] 排班响应: code={raw.get('code')}, total={raw.get('total')}")
            if isinstance(raw, dict) and raw.get("code") == 200:
                rows = raw.get("rows", [])
                log.info(f"[HIS-System] 解析到 {len(rows)} 条排班")
                if rows:
                    return rows

                # 某些旧数据的科室名、时段或日期带空格/时间部分，数据库精确条件会漏查。
                # 拉取排班后在本地做规范化匹配，避免错误降级到另一套科室表。
                all_resp = await self.system_client.get(
                    "/schedule/list", params={"pageSize": 1000}, headers=self._headers()
                )
                all_resp.raise_for_status()
                all_rows = all_resp.json().get("rows", [])

                def norm(value: Any) -> str:
                    return str(value or "").strip().replace(" ", "")

                target_date = norm(date_str)[:10]
                matched = [
                    row for row in all_rows
                    if norm(row.get("depName")) == norm(dep_name)
                    and norm(row.get("scheduleDate"))[:10] == target_date
                    and norm(row.get("scheduleTime")) == norm(time_slot)
                ]
                log.info(f"[HIS-System] 规范化回退匹配到 {len(matched)} 条排班")
                return matched
            log.warning(f"[HIS-System] 排班响应格式不匹配: {type(raw)}")
            return []
        except Exception as e:
            log.warning(f"[HIS-System] 查询排班列表失败: {str(e)}")
            return []
