package xian.woniuxy.reghos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.redis.utils.RedisUtils;
import com.ruoyi.system.api.domain.Schedule;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import xian.woniuxy.customer.dubboService.RemotePatientService;
import xian.woniuxy.domain.Patient;
import xian.woniuxy.domain.Register;
import xian.woniuxy.reghos.mapper.RegisterMapper;
import xian.woniuxy.reghos.service.GuahaoService;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuahaoServiceImpl implements GuahaoService {

    @DubboReference
    private final RemotePatientService remotePatientService;

    private final RegisterMapper registerMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    private Schedule _fetchSchedule(Long scheduleId) {
        try {
            Map<String, Object> resp = restTemplate.getForObject(
                "http://localhost:9201/schedule/" + scheduleId, Map.class
            );
            if (resp != null && resp.get("data") != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) resp.get("data");
                Schedule s = new Schedule();
                s.setScheduleId(Long.valueOf(data.get("scheduleId").toString()));
                s.setUserId(Long.valueOf(data.get("userId").toString()));
                s.setUserName((String) data.get("userName"));
                s.setDepId(Long.valueOf(data.get("depId").toString()));
                s.setDepName((String) data.get("depName"));
                s.setScheduleNum(Long.valueOf(data.get("scheduleNum").toString()));
                log.info("[Guahao] 远程排班查询成功: scheduleId={}, depName={}, doctor={}",
                         s.getScheduleId(), s.getDepName(), s.getUserName());
                return s;
            }
            log.warn("[Guahao] 远程排班返回格式异常: {}", resp);
        } catch (Exception e) {
            log.error("[Guahao] 远程排班查询失败: {}", e.getMessage());
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String guahao(Long scheduleId, String patientCardId) {
        Patient patient = remotePatientService.queryByCardId(patientCardId);
        if (patient == null) {
            return "noinfo";
        }
        Object o1 = RedisUtils.getCacheObject("guahaoexist:" + patientCardId);
        if (o1 != null) {
            return "exist";
        }

        Schedule schedule = _fetchSchedule(scheduleId);
        if (schedule == null) {
            return "noschedule";
        }

        int n = 0;
        Object o = RedisUtils.getCacheObject("guahaonum:" + scheduleId);
        if (o != null) {
            n = Integer.parseInt(o.toString());
        }
        if (n >= schedule.getScheduleNum()) {
            return "no";
        }
        Register register = new Register();
        register.setRegisterDate(new java.util.Date());
        register.setDepId(schedule.getDepId());
        register.setDepName(schedule.getDepName());
        register.setPatientId(patient.getPatientId());
        register.setUserId(schedule.getUserId());
        register.setUserName(schedule.getUserName());
        register.setRegisterPrice(BigDecimal.valueOf(5L));
        register.setRegisterState("0");
        register.setPatientName(patient.getPatientName());
        int insert = registerMapper.insert(register);
        RedisUtils.setCacheObject("guahaoexist:"+patientCardId, "true", Duration.ofHours(5));
        RedisUtils.incrAtomicValue("guahaonum:" + scheduleId);
        RedisUtils.setCacheObject("guahaopatient:"+patientCardId, register, Duration.ofHours(5));
        RedisUtils.setCacheObject("guahaoregister:"+register.getRegisterId(), register, Duration.ofHours(5));
        List<Register> registerList = RedisUtils.getCacheList("guahaolist:" + schedule.getUserId());
        if (registerList == null) {
            LinkedList<Register> list = new LinkedList<>();
            list.add(register);
            RedisUtils.setCacheList("guahaolist:" + schedule.getUserId(), list);
            RedisUtils.expire("guahaolist:" + schedule.getUserId(), Duration.ofHours(5));
        } else {
            registerList.add(register);
            RedisUtils.deleteKeys("guahaolist:" + schedule.getUserId());
            RedisUtils.setCacheList("guahaolist:" + schedule.getUserId(), registerList);
            RedisUtils.expire("guahaolist:" + schedule.getUserId(), Duration.ofHours(5));
        }
        return "ok";
    }
}
