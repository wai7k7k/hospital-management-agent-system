package xian.woniuxy.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import xian.woniuxy.customer.domain.bo.PrescriptionBo;
import xian.woniuxy.customer.domain.vo.PrescriptionVo;
import xian.woniuxy.customer.dubboService.RemoteEnterService;
import xian.woniuxy.customer.mapper.PrescriptionMapper;
import xian.woniuxy.customer.service.IPrescriptionService;

import xian.woniuxy.domain.Enter;
import xian.woniuxy.domain.Inspect;
import xian.woniuxy.domain.InspectOrder;
import xian.woniuxy.domain.Prescription;
import xian.woniuxy.reghos.dubboService.RemoteInspectOrderService;
import xian.woniuxy.reghos.dubboService.RemoteInspectService;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 处方Service业务层处理
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PrescriptionServiceImpl implements IPrescriptionService {

    private final PrescriptionMapper baseMapper;

    private final RabbitTemplate rabbitTemplate;

    @DubboReference
    private RemoteInspectService remoteInspectService;

    @DubboReference
    private RemoteInspectOrderService remoteInspectOrderService;

    @DubboReference
    private RemoteEnterService remoteEnterService;

    /**
     * 查询处方
     */
    @Override
    public PrescriptionVo queryById(Long prescriptionId) {
        return baseMapper.selectVoById(prescriptionId);
    }

    /**
     * 查询处方列表
     */
    @Override
    public TableDataInfo<PrescriptionVo> queryPageList(PrescriptionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Prescription> lqw = buildQueryWrapper(bo);
        Page<PrescriptionVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询处方列表
     */
    @Override
    public List<PrescriptionVo> queryList(PrescriptionBo bo) {
        LambdaQueryWrapper<Prescription> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Prescription> buildQueryWrapper(PrescriptionBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Prescription> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPatientId() != null, Prescription::getPatientId, bo.getPatientId());
        lqw.like(StringUtils.isNotBlank(bo.getPatientName()), Prescription::getPatientName, bo.getPatientName());
        lqw.eq(bo.getRegisterId() != null, Prescription::getRegisterId, bo.getRegisterId());
        lqw.eq(bo.getRecordId() != null, Prescription::getRecordId, bo.getRecordId());
        lqw.eq(bo.getDepId() != null, Prescription::getDepId, bo.getDepId());
        lqw.like(StringUtils.isNotBlank(bo.getDepName()), Prescription::getDepName, bo.getDepName());
        lqw.eq(bo.getPrescriptionDate() != null, Prescription::getPrescriptionDate, bo.getPrescriptionDate());
        lqw.eq(bo.getUserId() != null, Prescription::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getUserName()), Prescription::getUserName, bo.getUserName());
        lqw.eq(StringUtils.isNotBlank(bo.getRpMedicine()), Prescription::getRpMedicine, bo.getRpMedicine());
        lqw.eq(StringUtils.isNotBlank(bo.getRpInspect()), Prescription::getRpInspect, bo.getRpInspect());
        lqw.eq(StringUtils.isNotBlank(bo.getDiagnosis()), Prescription::getDiagnosis, bo.getDiagnosis());
        return lqw;
    }

    /**
     * 新增处方
     */
    @Override
    public Boolean insertByBo(PrescriptionBo bo) {
        Prescription add = BeanUtil.toBean(bo, Prescription.class);
        add.setPrescriptionDate(new Date());
        validEntityBeforeSave(add);
//        final String InspectName = add.getRpInspect();
//        if (InspectName != null && InspectName != "") {
//            //根据是否有检查项目名称查询检查项目表获取检查项目实例
//            final Inspect inspect = remoteInspectService.queryByInspectName(InspectName);
//            final Long inspectId = inspect.getInspectId();
//            //检查项目订单表实例
//            final InspectOrder inspectOrder = new InspectOrder();
//            //生成检查项目订单表
//            inspectOrder.setInspectId(inspectId);
//            inspectOrder.setInspectOrderDate(new Date());
//            inspectOrder.setPatientId(add.getPatientId());
//            inspectOrder.setUserId(add.getUserId());
//            inspectOrder.setRegisterId(add.getRegisterId());
//            //判断病人是否住院
//            final Enter enter = remoteEnterService.queryByPatientId(add.getPatientId());
//            if (enter != null) {
//                inspectOrder.setEnterId(enter.getEnterId());
//            }
//            inspectOrder.setInspectOrderPrice(inspect.getInspectPrice());
//            //添加检查项目订单表
//            remoteInspectOrderService.inster(inspectOrder);
//        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setPrescriptionId(add.getPrescriptionId());
            publishPrescriptionCreatedEvent(add);
        }
        return flag;
    }

    /**
     * 修改处方
     */
    @Override
    public Boolean updateByBo(PrescriptionBo bo) {
        Prescription update = BeanUtil.toBean(bo, Prescription.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Prescription entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 发布处方创建事件到 RabbitMQ
     */
    private void publishPrescriptionCreatedEvent(Prescription prescription) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("prescriptionId", prescription.getPrescriptionId());
            data.put("patientId", prescription.getPatientId());
            data.put("patientName", prescription.getPatientName());
            data.put("registerId", prescription.getRegisterId());
            data.put("recordId", prescription.getRecordId());
            data.put("depId", prescription.getDepId());
            data.put("depName", prescription.getDepName());
            data.put("userId", prescription.getUserId());
            data.put("userName", prescription.getUserName());
            data.put("diagnosis", prescription.getDiagnosis());
            data.put("notes", prescription.getRemark());
            data.put("prescriptionDate", prescription.getPrescriptionDate() != null
                    ? prescription.getPrescriptionDate().toString() : null);
            data.put("medications", parseRpMedicine(prescription.getRpMedicine()));

            Map<String, Object> envelope = new HashMap<>();
            envelope.put("eventId", "prescription-" + prescription.getPrescriptionId() + "-" + System.currentTimeMillis());
            envelope.put("eventType", "PRESCRIPTION_CREATED");
            envelope.put("source", "his-system");
            envelope.put("serviceId", "ylxm-customer");
            envelope.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            envelope.put("data", data);

            rabbitTemplate.convertAndSend("hospital.events", "prescription.created", envelope);
            log.info("处方创建事件已发布 - prescriptionId: {}", prescription.getPrescriptionId());
        } catch (Exception e) {
            log.error("发布处方创建事件失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 解析 rpMedicine 字符串为结构化药品列表
     * 格式: "药品A 5mg qd 30天; 药品B 10mg qn 30天"
     */
    private List<Map<String, String>> parseRpMedicine(String rpMedicine) {
        List<Map<String, String>> medications = new ArrayList<>();
        if (rpMedicine == null || rpMedicine.trim().isEmpty()) {
            return medications;
        }
        try {
            String[] items = rpMedicine.split(";");
            for (String item : items) {
                String trimmed = item.trim();
                if (trimmed.isEmpty()) continue;
                String[] parts = trimmed.split("\\s+");
                Map<String, String> med = new HashMap<>();
                if (parts.length >= 1) med.put("name", parts[0]);
                if (parts.length >= 2) med.put("dosage", parts[1]);
                if (parts.length >= 3) med.put("frequency", parts[2]);
                if (parts.length >= 4) med.put("duration", parts[3]);
                if (parts.length >= 5) {
                    StringBuilder rest = new StringBuilder();
                    for (int i = 4; i < parts.length; i++) {
                        if (rest.length() > 0) rest.append(" ");
                        rest.append(parts[i]);
                    }
                    med.put("notes", rest.toString());
                }
                if (!med.isEmpty()) {
                    medications.add(med);
                }
            }
        } catch (Exception e) {
            log.warn("解析 rpMedicine 失败，保留原始文本: {}", rpMedicine, e);
            Map<String, String> fallback = new HashMap<>();
            fallback.put("name", rpMedicine);
            fallback.put("dosage", "");
            fallback.put("frequency", "");
            fallback.put("duration", "");
            medications.add(fallback);
        }
        return medications;
    }

    /**
     * 批量删除处方
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
