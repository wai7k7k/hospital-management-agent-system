package xian.woniuxy.reghos.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.system.api.RemoteDeptService;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysDept;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.common.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xian.woniuxy.customer.dubboService.RemotePatientService;
import xian.woniuxy.domain.Patient;
import xian.woniuxy.reghos.domain.bo.RegisterBo;
import xian.woniuxy.reghos.domain.vo.OperationVo;
import xian.woniuxy.reghos.domain.vo.RegisterVo;
import xian.woniuxy.domain.Register;
import xian.woniuxy.reghos.mapper.RegisterMapper;
import xian.woniuxy.reghos.service.IRegisterService;

import java.time.Duration;
import java.util.*;

/**
 * 挂号Service业务层处理
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class RegisterServiceImpl implements IRegisterService {

    private final RegisterMapper baseMapper;
    private final RabbitTemplate rabbitTemplate;
    @DubboReference
    private final RemoteUserService remoteUserService;
    @DubboReference
    private final RemoteDeptService remoteDeptService;
    @DubboReference
    private final RemotePatientService remotePatientService;


    /**
     * 查询挂号
     */
    @Override
    public RegisterVo queryById(Long registerId){
        return baseMapper.selectVoById(registerId);
    }

    /**
     * 查询挂号列表
     */
    @Override
    public TableDataInfo<RegisterVo> queryPageList(RegisterBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Register> lqw = buildQueryWrapper(bo);
        Page<RegisterVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<SysDept> sysDepts = remoteDeptService.deptList();
        HashMap<Long, String> map = new HashMap<>(16);
        sysDepts.forEach(dept -> {
            map.put(dept.getDeptId(), dept.getDeptName());
        });
        List<RegisterVo> records = result.getRecords();
        records.forEach(registerVo -> {
            String deptName = map.get(registerVo.getDepId());
            if (deptName != null) {
                registerVo.setDepName(deptName);
            }
            if (registerVo.getDepName() == null) {
                registerVo.setDepName("无科室");
            }
            if (registerVo.getPatientId() != null) {
                Patient patient = remotePatientService.queryById(registerVo.getPatientId());
                if (patient != null) {
                    registerVo.setPatientName(patient.getPatientName());
                }
            }
        });
        return TableDataInfo.build(result);
    }

    /**
     * 查询挂号列表
     */
    @Override
    public List<RegisterVo> queryList(RegisterBo bo) {
        LambdaQueryWrapper<Register> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Register> buildQueryWrapper(RegisterBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Register> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, Register::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getUserName()), Register::getUserName, bo.getUserName());
        lqw.eq(bo.getRegisterDate() != null, Register::getRegisterDate, bo.getRegisterDate());
        lqw.like(StringUtils.isNotBlank(bo.getDepName()), Register::getDepName, bo.getDepName());
        return lqw;
    }

    /**
     * 新增挂号
     */
    @Override
    public Boolean insertByBo(RegisterBo bo) {
        // 查找医生信息
        SysUser user = remoteUserService.queryByUserName(bo.getUserName());
        if (user != null) {
            bo.setUserId(user.getUserId());
        }

        // 查找科室信息 — 统一走 ruoyi-system 的 sys_dept 表（废弃本地 dep 表）
        SysDept sysDept = remoteDeptService.selectByDeptName(bo.getDepName());
        if (sysDept != null) {
            bo.setDepId(sysDept.getDeptId());
        } else {
            // fallback: 查询本地 dep 表（存量数据兼容）
            Map<String, Object> depMap = baseMapper.selectDepByName(bo.getDepName());
            if (depMap != null) {
                Object depId = depMap.get("depId");
                if (depId instanceof Number) {
                    bo.setDepId(((Number) depId).longValue());
                }
            }
        }

        Register add = BeanUtil.toBean(bo, Register.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setRegisterId(add.getRegisterId());
            publishRegisterCreatedEvent(add);

            // 设置 Redis 缓存，供就诊页身份证查询使用
            try {
                Patient patient = remotePatientService.queryById(add.getPatientId());
                if (patient != null && patient.getPatientCardId() != null) {
                    RedisUtils.setCacheObject("guahaopatient:" + patient.getPatientCardId(), add, Duration.ofHours(5));
                    RedisUtils.setCacheObject("guahaoregister:" + add.getRegisterId(), add, Duration.ofHours(5));
                }
                // 加入 guahaolist 供就诊页右侧列表展示（按 userId 分组，AI 挂号 userId=0）
                Long doctorUserId = add.getUserId() != null ? add.getUserId() : 0L;
                List<Register> registerList = RedisUtils.getCacheList("guahaolist:" + doctorUserId);
                if (registerList == null) {
                    registerList = new java.util.LinkedList<>();
                }
                registerList.add(add);
                RedisUtils.deleteKeys("guahaolist:" + doctorUserId);
                RedisUtils.setCacheList("guahaolist:" + doctorUserId, registerList);
                RedisUtils.expire("guahaolist:" + doctorUserId, Duration.ofHours(5));
            } catch (Exception e) {
                // Redis 缓存失败不影响主流程
                System.err.println("Redis 缓存设置失败: " + e.getMessage());
            }
        }
        return flag;
    }

    /**
     * 修改挂号
     */
    @Override
    public Boolean updateByBo(RegisterBo bo) {
        Register update = BeanUtil.toBean(bo, Register.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Register entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 发布挂号创建事件
     */
    private void publishRegisterCreatedEvent(Register register) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "REGISTER_CREATED");
            event.put("registerId", register.getRegisterId());
            event.put("patientId", register.getPatientId());
            event.put("depName", register.getDepName());
            event.put("registerDate", register.getRegisterDate());
            event.put("userId", register.getUserId());
            event.put("userName", register.getUserName());

            rabbitTemplate.convertAndSend("hospital.events", "register.created", event);
            System.out.println("挂号创建事件已发布: " + event);
        } catch (Exception e) {
            System.err.println("发布挂号创建事件失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 批量删除挂号
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public void endReg(Long registerId) {
        Register register =  RedisUtils.getCacheObject("guahaoregister:" + registerId);
        Long patientId = register.getPatientId();
        Patient patient = remotePatientService.queryById(patientId);
        RedisUtils.deleteKeys("guahaoregister:" + registerId);
        RedisUtils.deleteKeys("guahaoexist:"+patient.getPatientCardId());
        RedisUtils.deleteKeys("guahaopatient:"+patient.getPatientCardId());
        List<Register> list = RedisUtils.getCacheList("guahaolist:" + register.getUserId());
        list.removeIf(register1 -> registerId.equals(register1.getRegisterId()));
        RedisUtils.deleteKeys("guahaolist:" + register.getUserId());
        if (!list.isEmpty()) {
            RedisUtils.setCacheList("guahaolist:" + register.getUserId(), list);
            RedisUtils.expire("guahaolist:" + register.getUserId(), Duration.ofHours(5));
        }
        Register register1 = baseMapper.selectById(registerId);
        register1.setRegisterState("1");
        baseMapper.updateById(register1);
    }

    @Override
    public void doGuoHao(Long registerId) {
        Register register = RedisUtils.getCacheObject("guahaoregister:" + registerId);
        List<Register> list = RedisUtils.getCacheList("guahaolist:" + register.getUserId());
        list.removeIf(register1 -> registerId.equals(register1.getRegisterId()));
        list.add(register);
        RedisUtils.deleteKeys("guahaolist:" + register.getUserId());
        RedisUtils.setCacheList("guahaolist:" + register.getUserId(), list);
        RedisUtils.expire("guahaolist:" + register.getUserId(), Duration.ofHours(5));
    }

    @Override
    public RegisterVo queryByCardId(String cardId) {
        Patient patient = remotePatientService.queryByCardId(cardId);
        if (patient == null) {
            return null;
        }
        LambdaQueryWrapper<Register> lqw = Wrappers.lambdaQuery();
        lqw.eq(Register::getPatientId, patient.getPatientId());
        lqw.orderByDesc(Register::getRegisterDate);
        lqw.last("LIMIT 1");
        List<Register> list = baseMapper.selectList(lqw);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return BeanUtil.toBean(list.get(0), RegisterVo.class);
    }
}
