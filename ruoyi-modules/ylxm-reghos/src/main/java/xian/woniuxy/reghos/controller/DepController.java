package xian.woniuxy.reghos.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.ResultVO;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.core.validate.QueryGroup;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.excel.utils.ExcelUtil;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.system.api.domain.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xian.woniuxy.reghos.domain.vo.DepVo;
import xian.woniuxy.reghos.domain.bo.DepBo;
import xian.woniuxy.reghos.mapper.RegisterMapper;
import xian.woniuxy.reghos.service.IDepService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 科室;控制器
 * 前端访问路由地址为:/reghos-serv/dep
 *
 * @author G
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/dep")
public class DepController extends BaseController {

    private final IDepService iDepService;
    @Autowired
    private RegisterMapper registerMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询科室;列表
     */
//    @SaCheckPermission("reghos-serv:dep:list")
    @GetMapping("/list")
    public TableDataInfo<DepVo> list(DepBo bo, PageQuery pageQuery) {
        return iDepService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出科室;列表
     */
//    @SaCheckPermission("reghos-serv:dep:export")
    @Log(title = "科室;", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DepBo bo, HttpServletResponse response) {
        List<DepVo> list = iDepService.queryList(bo);
        ExcelUtil.exportExcel(list, "科室;", DepVo.class, response);
    }

    /**
     * 获取科室;详细信息
     *
     * @param depId 主键
     */
//    @SaCheckPermission("reghos-serv:dep:query")
    @GetMapping("/{depId}")
    public R<DepVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long depId) {
        return R.ok(iDepService.queryById(depId));
    }

    /**
     * 新增科室;
     */
//    @SaCheckPermission("reghos-serv:dep:add")
    @Log(title = "科室;", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DepBo bo) {
        return toAjax(iDepService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改科室;
     */
//    @SaCheckPermission("reghos-serv:dep:edit")
    @Log(title = "科室;", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DepBo bo) {
        return toAjax(iDepService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除科室;
     *
     * @param depIds 主键串
     */
//    @SaCheckPermission("reghos-serv:dep:remove")
    @Log(title = "科室;", businessType = BusinessType.DELETE)
    @DeleteMapping("/{depIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] depIds) {
        return toAjax(iDepService.deleteWithValidByIds(Arrays.asList(depIds), true) ? 1 : 0);
    }

    /**
     * 查询科室可用排班（公开接口，供 AI 服务调用）
     */
    @GetMapping("/{depId}/schedules")
    public ResultVO getSchedules(
        @NotNull(message = "主键不能为空") @PathVariable Long depId,
        @RequestParam String date,
        @RequestParam String timeSlot
    ) {
        ensureScheduleTable();
        List<Schedule> schedules = registerMapper.selectSchedulesByDepAndDate(depId, date, timeSlot);
        // 查询已挂号人数，计算剩余名额
        for (Schedule s : schedules) {
            Integer cnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM register WHERE dep_id = ? AND DATE(register_date) = ? AND register_state = '0'",
                Integer.class, s.getDepId(), date
            );
            s.setShengyu(s.getScheduleNum() - (cnt != null ? cnt : 0));
        }
        return ResultVO.success().data("schedules", schedules);
    }

    /**
     * 初始化排班和医生数据（一次性调用）
     * POST /dep/init-schedules
     * Body: {"depId": 1, "depName": "耳鼻喉科"}
     */
    @PostMapping("/init-schedules")
    public ResultVO initSchedules(@RequestBody Map<String, Object> body) {
        try {
            Long depId = Long.valueOf(body.get("depId").toString());
            String depName = (String) body.get("depName");
            ensureScheduleTable();

            // 1. 在 ry_cloud.sys_user 中创建医生（如果不存在）
            List<Map<String, Object>> existing = jdbcTemplate.queryForList(
                "SELECT user_id, user_name FROM ry_cloud.sys_user WHERE user_name = ?", "doc_wang"
            );
            Long docId;
            if (existing.isEmpty()) {
                String insertSql = "INSERT INTO ry_cloud.sys_user (user_id, dept_id, user_name, nick_name, " +
                    "email, phonenumber, sex, password, status, del_flag, create_by, create_time) " +
                    "SELECT IFNULL(MAX(user_id),0)+1, ?, ?, ?, 'doctor@hospital.com', '13900000001', '0', " +
                    "'$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', NOW() " +
                    "FROM ry_cloud.sys_user";
                jdbcTemplate.update(insertSql, depId, "doc_wang", "王医生");
                docId = jdbcTemplate.queryForObject("SELECT MAX(user_id) FROM ry_cloud.sys_user WHERE user_name = ?",
                    Long.class, "doc_wang");
            } else {
                docId = Long.valueOf(existing.get(0).get("user_id").toString());
            }

            // 2. 创建另一个医生
            List<Map<String, Object>> existing2 = jdbcTemplate.queryForList(
                "SELECT user_id FROM ry_cloud.sys_user WHERE user_name = ?", "doc_li"
            );
            Long docId2;
            if (existing2.isEmpty()) {
                String insertSql2 = "INSERT INTO ry_cloud.sys_user (user_id, dept_id, user_name, nick_name, " +
                    "email, phonenumber, sex, password, status, del_flag, create_by, create_time) " +
                    "SELECT IFNULL(MAX(user_id),0)+1, ?, ?, ?, 'doctor2@hospital.com', '13900000002', '1', " +
                    "'$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', NOW() " +
                    "FROM ry_cloud.sys_user";
                jdbcTemplate.update(insertSql2, depId, "doc_li", "李医生");
                docId2 = jdbcTemplate.queryForObject("SELECT MAX(user_id) FROM ry_cloud.sys_user WHERE user_name = ?",
                    Long.class, "doc_li");
            } else {
                docId2 = Long.valueOf(existing2.get(0).get("user_id").toString());
            }

            // 3. 创建排班记录（未来7天，每天上午/下午各一条）
            int created = 0;
            String[] timeSlots = {"上午", "下午"};
            for (int i = 0; i < 7; i++) {
                LocalDate d = LocalDate.now().plusDays(i);
                for (String ts : timeSlots) {
                    // 检查是否已有
                    Integer exist = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM schedule WHERE dep_id = ? AND schedule_date = ? AND schedule_time = ?",
                        Integer.class, depId, d.toString(), ts
                    );
                    if (exist == null || exist == 0) {
                        jdbcTemplate.update(
                            "INSERT INTO schedule (user_id, user_name, dep_id, dep_name, schedule_date, schedule_time, " +
                            "schedule_num, create_by, create_time) VALUES (?, ?, ?, ?, ?, ?, ?, 'admin', NOW())",
                            docId, "王医生", depId, depName, d.toString(), ts, 45
                        );
                        created++;
                    }
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("created_schedules", created);
            result.put("doctor_1_name", "王医生");
            result.put("doctor_1_id", docId);
            result.put("doctor_2_name", "李医生");
            result.put("doctor_2_id", docId2);
            return ResultVO.success().data("result", result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.success("初始化失败: " + e.getMessage());
        }
    }

    /**
     * 初始化检查结果相关表结构（一次性调用）
     * POST /dep/init-exam-tables
     */
    @PostMapping("/init-exam-tables")
    public ResultVO initExamTables() {
        try {
            ensureExamResultTables();
            return ResultVO.success("检查结果表初始化完成");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.success("初始化失败: " + e.getMessage());
        }
    }

    private void ensureExamResultTables() {
        // 给 inspect_order 加 status（如果不存在）
        try {
            jdbcTemplate.execute("ALTER TABLE inspect_order ADD COLUMN status VARCHAR(20) DEFAULT '待采样'");
        } catch (Exception e) { /* 字段已存在 */ }

        // 给 inspect 加 item_type（如果不存在）
        try {
            jdbcTemplate.execute("ALTER TABLE inspect ADD COLUMN item_type VARCHAR(20) DEFAULT 'NUMERIC'");
        } catch (Exception e) { /* 字段已存在 */ }

        // 主表 exam_result
        jdbcTemplate.execute(
            "CREATE TABLE IF NOT EXISTS exam_result (" +
            "  id bigint NOT NULL AUTO_INCREMENT," +
            "  apply_id bigint DEFAULT NULL COMMENT '检查申请单ID(inspect_order_id)'," +
            "  patient_id bigint DEFAULT NULL COMMENT '患者ID'," +
            "  register_id bigint DEFAULT NULL COMMENT '挂号ID'," +
            "  item_type varchar(20) DEFAULT 'NUMERIC' COMMENT 'NUMERIC数值指标/IMAGE_TEXT影像文本'," +
            "  report_time datetime DEFAULT NULL COMMENT '报告出具时间'," +
            "  doctor_id bigint DEFAULT NULL COMMENT '出报告医生ID'," +
            "  summary_conclusion varchar(1000) DEFAULT NULL COMMENT '通用结论'," +
            "  create_by varchar(64) DEFAULT ''," +
            "  create_time datetime DEFAULT NULL," +
            "  update_by varchar(64) DEFAULT ''," +
            "  update_time datetime DEFAULT NULL," +
            "  remark varchar(500) DEFAULT NULL," +
            "  PRIMARY KEY (id)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通用检查结果主表'"
        );

        // 子表 数值指标
        jdbcTemplate.execute(
            "CREATE TABLE IF NOT EXISTS exam_result_index (" +
            "  id bigint NOT NULL AUTO_INCREMENT," +
            "  result_id bigint NOT NULL COMMENT '关联exam_result.id'," +
            "  index_name varchar(100) DEFAULT NULL COMMENT '指标名称'," +
            "  value varchar(50) DEFAULT NULL COMMENT '测定数值'," +
            "  reference varchar(100) DEFAULT NULL COMMENT '参考范围'," +
            "  is_abnormal tinyint DEFAULT 0 COMMENT '是否异常 0正常/1异常'," +
            "  PRIMARY KEY (id)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数值型检查指标明细'"
        );

        // 子表 影像文本
        jdbcTemplate.execute(
            "CREATE TABLE IF NOT EXISTS exam_result_image_text (" +
            "  id bigint NOT NULL AUTO_INCREMENT," +
            "  result_id bigint NOT NULL COMMENT '关联exam_result.id'," +
            "  check_content text DEFAULT NULL COMMENT '检查所见完整文本'," +
            "  check_conclusion varchar(1000) DEFAULT NULL COMMENT '检查结论'," +
            "  PRIMARY KEY (id)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='影像文本型检查结果'"
        );

        // 初始化几个常用检查项目的 item_type
        jdbcTemplate.execute(
            "UPDATE inspect SET item_type = 'NUMERIC' WHERE inspect_name IN ('血常规','血糖','肝功能','尿常规','血脂') AND (item_type IS NULL OR item_type = 'NUMERIC')"
        );
        jdbcTemplate.execute(
            "UPDATE inspect SET item_type = 'IMAGE_TEXT' WHERE inspect_name IN ('CT','DR','核磁','B超','内镜','鼻窦扫描','X光') AND (item_type IS NULL OR item_type = 'NUMERIC')"
        );
    }

    private void ensureScheduleTable() {
        try {
            jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS schedule (" +
                "  schedule_id bigint NOT NULL AUTO_INCREMENT," +
                "  user_id bigint DEFAULT NULL," +
                "  user_name varchar(20) DEFAULT NULL," +
                "  dep_id bigint DEFAULT NULL," +
                "  dep_name varchar(20) DEFAULT NULL," +
                "  avatar varchar(100) DEFAULT NULL," +
                "  schedule_date date DEFAULT NULL," +
                "  schedule_time varchar(5) DEFAULT NULL," +
                "  schedule_num int DEFAULT NULL," +
                "  create_by varchar(64) DEFAULT ''," +
                "  create_time datetime DEFAULT NULL," +
                "  update_by varchar(64) DEFAULT ''," +
                "  update_time datetime DEFAULT NULL," +
                "  remark varchar(500) DEFAULT NULL," +
                "  PRIMARY KEY (schedule_id)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
            );
        } catch (Exception e) {
            // table may already exist in another DB
        }
    }

    
    @PostMapping("/init-menu")
    public ResultVO initMenu() {
        try {
            // visible='1' means HIDDEN in RuoYi convention
            jdbcTemplate.update(
                "UPDATE ry_cloud.sys_menu SET visible = '1' WHERE menu_name = '血糖结果'");
            jdbcTemplate.update(
                "UPDATE ry_cloud.sys_menu SET visible = '1' WHERE menu_name LIKE '%血常规%'");

            // Delete old record + role mapping, then recreate
            jdbcTemplate.update(
                "DELETE FROM ry_cloud.sys_role_menu WHERE menu_id IN (SELECT menu_id FROM ry_cloud.sys_menu WHERE menu_name = '通用检查结果')");
            jdbcTemplate.update(
                "DELETE FROM ry_cloud.sys_menu WHERE menu_name = '通用检查结果'");

            Long newId = jdbcTemplate.queryForObject(
                "SELECT IFNULL(MAX(menu_id), 1000000000000000000) + 1 FROM ry_cloud.sys_menu",
                Long.class);
            // parent_id=3 = directory; order_num=6 puts it after inspect_orderr
            jdbcTemplate.update(
                "INSERT INTO ry_cloud.sys_menu (menu_id, menu_name, parent_id, order_num, path, component, "
                + "is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time) "
                + "VALUES (?, '通用检查结果', 3, 6, 'exam_result', "
                + "'reghos-serv/exam_result/index', 1, 0, 'C', '0', '0', "
                + "'reghos-serv:exam_result:list', 'chart', 'admin', NOW())",
                newId);

            jdbcTemplate.update(
                "INSERT INTO ry_cloud.sys_role_menu (role_id, menu_id) VALUES (1, ?)", newId);

            try {
                jdbcTemplate.execute(
                    "ALTER TABLE inspect_order MODIFY enter_id BIGINT NULL DEFAULT NULL "
                    + "COMMENT '住院ID，门诊可为空'");
            } catch (Exception e) { }

            Map<String, Object> result = new HashMap<>();
            result.put("msg", "菜单重建完成（parent=3, path=exam_result）");
            result.put("new_menu_id", newId);
            return ResultVO.success().data("result", result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.success("初始化失败: " + e.getMessage());
        }
    }

    @GetMapping("/debug-menu")
    public ResultVO debugMenu() {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT menu_id, menu_name, parent_id, path, component, visible, menu_type, perms "
                + "FROM ry_cloud.sys_menu "
                + "WHERE menu_type = 'M' OR parent_id IN (SELECT menu_id FROM ry_cloud.sys_menu WHERE menu_type = 'M') "
                + "ORDER BY parent_id, order_num LIMIT 50");
            return ResultVO.success().data("menus", rows);
        } catch (Exception e) {
            return ResultVO.success("error: " + e.getMessage());
        }
    }

    /**
     * 初始化系统管理菜单 + 医生角色 + 患者角色
     * POST /dep/init-system-menu
     */
    @PostMapping("/init-system-menu")
    public ResultVO initSystemMenu() {
        Map<String, Object> r = new HashMap<>();
        try {
            // ═══════ 1. 恢复系统管理菜单 ═══════
            // 系统管理 (parent M目录)
            jdbcTemplate.update(
                "UPDATE ry_cloud.sys_menu SET visible = '0' WHERE menu_name IN "
                + "('系统管理', '用户管理', '角色管理', '菜单管理', '部门管理', '岗位管理', '字典管理', '参数设置', '通知公告', '日志管理')");

            // 确保管理员角色(role_id=1)有所有菜单权限
            jdbcTemplate.update(
                "INSERT IGNORE INTO ry_cloud.sys_role_menu (role_id, menu_id) "
                + "SELECT 1, menu_id FROM ry_cloud.sys_menu "
                + "WHERE menu_id NOT IN (SELECT menu_id FROM ry_cloud.sys_role_menu WHERE role_id = 1)");
            r.put("step1", "系统管理菜单已恢复");

            // ═══════ 2. 创建医生角色 ═══════
            Integer drCnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ry_cloud.sys_role WHERE role_key = 'doctor'", Integer.class);
            Long doctorRoleId;
            if (drCnt != null && drCnt > 0) {
                doctorRoleId = jdbcTemplate.queryForObject(
                    "SELECT role_id FROM ry_cloud.sys_role WHERE role_key = 'doctor' LIMIT 1", Long.class);
            } else {
                Long newRoleId = jdbcTemplate.queryForObject(
                    "SELECT IFNULL(MAX(role_id), 100) + 1 FROM ry_cloud.sys_role", Long.class);
                jdbcTemplate.update(
                    "INSERT INTO ry_cloud.sys_role (role_id, role_name, role_key, role_sort, data_scope, status, del_flag, create_by, create_time) "
                    + "VALUES (?, '医生', 'doctor', 2, '5', '0', '0', 'admin', NOW())", newRoleId);
                doctorRoleId = newRoleId;
            }

            // 给医生角色分配菜单：检查项目目录 + 检查项目订单 + 通用检查结果 + 检查项目
            List<Long> doctorMenuIds = jdbcTemplate.queryForList(
                "SELECT menu_id FROM ry_cloud.sys_menu WHERE menu_name IN "
                + "('检查项目', '检查项目订单', '通用检查结果', '检查项目')",
                Long.class);
            for (Long mid : doctorMenuIds) {
                Integer exists = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM ry_cloud.sys_role_menu WHERE role_id = ? AND menu_id = ?",
                    Integer.class, doctorRoleId, mid);
                if (exists == null || exists == 0) {
                    jdbcTemplate.update(
                        "INSERT INTO ry_cloud.sys_role_menu (role_id, menu_id) VALUES (?, ?)",
                        doctorRoleId, mid);
                }
            }
            r.put("step2", "医生角色已就绪, role_id=" + doctorRoleId);

            // ═══════ 3. 创建患者角色 ═══════
            Integer ptCnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ry_cloud.sys_role WHERE role_key = 'patient'", Integer.class);
            Long patientRoleId;
            if (ptCnt != null && ptCnt > 0) {
                patientRoleId = jdbcTemplate.queryForObject(
                    "SELECT role_id FROM ry_cloud.sys_role WHERE role_key = 'patient' LIMIT 1", Long.class);
            } else {
                Long newRoleId = jdbcTemplate.queryForObject(
                    "SELECT IFNULL(MAX(role_id), 100) + 1 FROM ry_cloud.sys_role", Long.class);
                jdbcTemplate.update(
                    "INSERT INTO ry_cloud.sys_role (role_id, role_name, role_key, role_sort, data_scope, status, del_flag, create_by, create_time) "
                    + "VALUES (?, '患者', 'patient', 3, '5', '0', '0', 'admin', NOW())", newRoleId);
                patientRoleId = newRoleId;
            }

            // 患者角色只分配首页
            Long indexMenuId = jdbcTemplate.queryForObject(
                "SELECT menu_id FROM ry_cloud.sys_menu WHERE menu_name = '首页' LIMIT 1", Long.class);
            if (indexMenuId != null) {
                jdbcTemplate.update(
                    "INSERT IGNORE INTO ry_cloud.sys_role_menu (role_id, menu_id) VALUES (?, ?)",
                    patientRoleId, indexMenuId);
            }
            r.put("step3", "患者角色已就绪, role_id=" + patientRoleId);

            // ═══════ 4. 创建示例医生账号 ═══════
            Integer duCnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ry_cloud.sys_user WHERE user_name = 'doctor01'", Integer.class);
            if (duCnt == null || duCnt == 0) {
                Long newUid = jdbcTemplate.queryForObject(
                    "SELECT IFNULL(MAX(user_id), 1000) + 1 FROM ry_cloud.sys_user", Long.class);
                jdbcTemplate.update(
                    "INSERT INTO ry_cloud.sys_user (user_id, dept_id, user_name, nick_name, user_type, email, "
                    + "phonenumber, sex, password, status, del_flag, create_by, create_time) "
                    + "VALUES (?, 103, 'doctor01', '王医生', 'sys_user', 'doctor01@hospital.com', "
                    + "'13900000001', '0', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', "
                    + "'0', '0', 'admin', NOW())", newUid);
                // 分配医生角色
                jdbcTemplate.update(
                    "INSERT INTO ry_cloud.sys_user_role (user_id, role_id) VALUES (?, ?)",
                    newUid, doctorRoleId);
                r.put("step4", "示例医生账号已创建: doctor01 / 123456");
            } else {
                r.put("step4", "doctor01 已存在，跳过创建");
            }

            // ═══════ 5. 恢复就诊/病历相关菜单 ═══════
            jdbcTemplate.update(
                "UPDATE ry_cloud.sys_menu SET visible = '0' WHERE menu_name IN "
                + "('患者管理', '处方管理', '住院管理', '病历管理', '自助挂号')");

            r.put("step5", "就诊相关菜单已恢复");
            r.put("doctor_role_id", doctorRoleId);
            r.put("patient_role_id", patientRoleId);
            return ResultVO.success().data("result", r);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.success().data("result", "初始化失败: " + e.getMessage());
        }
    }
}
