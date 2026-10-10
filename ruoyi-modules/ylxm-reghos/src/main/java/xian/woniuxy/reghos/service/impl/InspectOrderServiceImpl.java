package xian.woniuxy.reghos.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import xian.woniuxy.domain.Inspect;
import xian.woniuxy.domain.InspectOrder;
import xian.woniuxy.reghos.domain.bo.InspectOrderBo;
import xian.woniuxy.reghos.domain.vo.InspectOrderVo;
import xian.woniuxy.reghos.dubboService.RemoteInspectOrderService;
import xian.woniuxy.reghos.mapper.InspectMapper;
import xian.woniuxy.reghos.mapper.InspectOrderMapper;
import xian.woniuxy.reghos.service.IInspectOrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 检查项目订单;Service业务层处理
 *
 * @author G
 * @date 2022-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class InspectOrderServiceImpl implements IInspectOrderService {

    private final InspectOrderMapper baseMapper;

    private final RabbitTemplate rabbitTemplate;

    @NonNull
    private InspectMapper inspectMapper;

    @DubboReference
    private RemoteUserService remoteUserService;

    /**
     * 查询检查项目订单;
     */
    @Override
    public InspectOrderVo queryById(Long inspectOrderId) {
        return baseMapper.selectVoById(inspectOrderId);
    }

    /**
     * 查询检查项目订单;列表
     */
    @Override
    public TableDataInfo<InspectOrderVo> queryPageList(InspectOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<InspectOrder> lqw = buildQueryWrapper(bo);
        Page<InspectOrderVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        Map<Long,String> map = new HashMap();
        final List<Inspect> inspects = inspectMapper.selectList();
        inspects.forEach(inspect->{map.put(inspect.getInspectId(),inspect.getInspectName());});
        result.getRecords().forEach(record->{record.setInspectName(map.get(record.getInspectId()));});
        Map<Long,String> map2 = new HashMap();
        final List<SysUser> sysUsers = remoteUserService.queryList();
        sysUsers.forEach(user->{map2.put(user.getUserId(),user.getUserName());});
        result.getRecords().forEach(record->{record.setUserName(map2.get(record.getUserId()));});
        return TableDataInfo.build(result);
    }

    /**
     * 查询检查项目订单;列表
     */
    @Override
    public List<InspectOrderVo> queryList(InspectOrderBo bo) {
        LambdaQueryWrapper<InspectOrder> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<InspectOrder> buildQueryWrapper(InspectOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<InspectOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPatientId() != null, InspectOrder::getPatientId, bo.getPatientId());
        lqw.eq(bo.getUserId() != null, InspectOrder::getUserId, bo.getUserId());
        lqw.eq(bo.getRegisterId() != null, InspectOrder::getRegisterId, bo.getRegisterId());
        lqw.eq(bo.getEnterId() != null, InspectOrder::getEnterId, bo.getEnterId());
        lqw.eq(bo.getInspectId() != null, InspectOrder::getInspectId, bo.getInspectId());
        lqw.eq(bo.getInspectOrderDate() != null, InspectOrder::getInspectOrderDate, bo.getInspectOrderDate());
        lqw.eq(bo.getInspectOrderPrice() != null, InspectOrder::getInspectOrderPrice, bo.getInspectOrderPrice());
        lqw.like(StringUtils.isNotBlank(bo.getPatientName()), InspectOrder::getPatientName, bo.getPatientName());
        return lqw;
    }

    /**
     * 新增检查项目订单;
     */
    @Override
    public Boolean insertByBo(InspectOrderBo bo) {
        InspectOrder add = BeanUtil.toBean(bo, InspectOrder.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setInspectOrderId(add.getInspectOrderId());
            publishExaminationOrderedEvent(add);
        }
        return flag;
    }

    /**
     * 修改检查项目订单;（支持 enterId 设为 null）
     */
    @Override
    public Boolean updateByBo(InspectOrderBo bo) {
        InspectOrder update = BeanUtil.toBean(bo, InspectOrder.class);
        validEntityBeforeSave(update);
        // 用 UpdateWrapper 允许 enterId 更新为 null（MyBatis-Plus updateById 默认跳过 null 字段）
        LambdaUpdateWrapper<InspectOrder> uw = Wrappers.lambdaUpdate();
        uw.eq(InspectOrder::getInspectOrderId, update.getInspectOrderId());
        uw.set(InspectOrder::getPatientId, update.getPatientId());
        uw.set(InspectOrder::getUserId, update.getUserId());
        uw.set(InspectOrder::getRegisterId, update.getRegisterId());
        uw.set(InspectOrder::getEnterId, update.getEnterId());
        uw.set(InspectOrder::getInspectId, update.getInspectId());
        uw.set(InspectOrder::getInspectOrderDate, update.getInspectOrderDate());
        uw.set(InspectOrder::getInspectOrderPrice, update.getInspectOrderPrice());
        uw.set(InspectOrder::getPatientName, update.getPatientName());
        uw.set(InspectOrder::getRemark, update.getRemark());
        return baseMapper.update(null, uw) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(InspectOrder entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    private void publishExaminationOrderedEvent(InspectOrder order) {
        try {
            Inspect inspect = inspectMapper.selectById(order.getInspectId());
            String inspectName = inspect != null ? inspect.getInspectName() : "";
            String inspectType = inspect != null ? inspect.getInspectTableName() : "";

            Map<String, Object> data = new HashMap<>();
            data.put("inspectOrderId", order.getInspectOrderId());
            data.put("patientId", order.getPatientId());
            data.put("patientName", order.getPatientName());
            data.put("inspectId", order.getInspectId());
            data.put("inspectName", inspectName);
            data.put("inspectType", inspectType);
            data.put("userId", order.getUserId());
            data.put("userName", getDoctorName(order.getUserId()));
            data.put("registerId", order.getRegisterId());
            data.put("enterId", order.getEnterId());
            data.put("orderDate", order.getInspectOrderDate() != null
                    ? order.getInspectOrderDate().toString() : null);
            data.put("instructions", order.getRemark());

            Map<String, Object> envelope = new HashMap<>();
            envelope.put("eventId", "exam-" + order.getInspectOrderId() + "-" + System.currentTimeMillis());
            envelope.put("eventType", "EXAMINATION_ORDERED");
            envelope.put("source", "his-system");
            envelope.put("serviceId", "ylxm-reghos");
            envelope.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            envelope.put("data", data);

            rabbitTemplate.convertAndSend("hospital.events", "examination.ordered", envelope);
            log.info("检查开单事件已发布 - inspectOrderId: {}, patientId: {}", order.getInspectOrderId(), order.getPatientId());
        } catch (Exception e) {
            log.error("发布检查开单事件失败: {}", e.getMessage(), e);
        }
    }

    private String getDoctorName(Long userId) {
        try {
            if (userId == null) return "";
            SysUser user = remoteUserService.queryByUid(userId);
            return user != null ? user.getUserName() : "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 批量删除检查项目订单;
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
