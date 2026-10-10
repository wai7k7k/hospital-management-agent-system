package xian.woniuxy.reghos.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xian.woniuxy.domain.Inspect;
import xian.woniuxy.domain.InspectOrder;
import xian.woniuxy.reghos.domain.ExamResult;
import xian.woniuxy.reghos.domain.ExamResultIndex;
import xian.woniuxy.reghos.domain.ExamResultImageText;
import xian.woniuxy.reghos.domain.bo.ExamResultBo;
import xian.woniuxy.reghos.domain.vo.ExamResultVo;
import xian.woniuxy.reghos.mapper.ExamResultMapper;
import xian.woniuxy.reghos.mapper.InspectMapper;
import xian.woniuxy.reghos.mapper.InspectOrderMapper;
import xian.woniuxy.reghos.service.IExamResultService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamResultServiceImpl implements IExamResultService {

    private final ExamResultMapper baseMapper;
    private final InspectOrderMapper inspectOrderMapper;
    private final InspectMapper inspectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public ExamResultVo queryById(Long id) {
        ExamResultVo vo = baseMapper.selectVoById(id);
        if (vo != null) fillDetail(vo);
        return vo;
    }

    @Override
    public TableDataInfo<ExamResultVo> queryPageList(ExamResultBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ExamResult> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPatientId() != null, ExamResult::getPatientId, bo.getPatientId());
        lqw.eq(bo.getRegisterId() != null, ExamResult::getRegisterId, bo.getRegisterId());
        lqw.eq(bo.getApplyId() != null, ExamResult::getApplyId, bo.getApplyId());
        lqw.eq(StringUtils.isNotBlank(bo.getItemType()), ExamResult::getItemType, bo.getItemType());
        lqw.orderByDesc(ExamResult::getCreateTime);
        Page<ExamResultVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        page.getRecords().forEach(this::fillDetail);
        return TableDataInfo.build(page);
    }

    @Override
    public List<ExamResultVo> queryByPatientId(Long patientId) {
        LambdaQueryWrapper<ExamResult> lqw = Wrappers.lambdaQuery();
        lqw.eq(ExamResult::getPatientId, patientId);
        lqw.orderByDesc(ExamResult::getCreateTime);
        List<ExamResultVo> list = baseMapper.selectVoList(lqw);
        list.forEach(this::fillDetail);
        return list;
    }

    @Override
    public List<ExamResultVo> queryByRegisterId(Long registerId) {
        LambdaQueryWrapper<ExamResult> lqw = Wrappers.lambdaQuery();
        lqw.eq(ExamResult::getRegisterId, registerId);
        lqw.orderByDesc(ExamResult::getCreateTime);
        List<ExamResultVo> list = baseMapper.selectVoList(lqw);
        list.forEach(this::fillDetail);
        return list;
    }

    private void fillDetail(ExamResultVo vo) {
        if (vo == null) return;
        List<ExamResultIndex> indices = baseMapper.selectIndicesByResultId(vo.getId());
        if (indices != null && !indices.isEmpty()) {
            vo.setIndices(indices.stream().map(idx -> {
                ExamResultVo.ExamResultIndexVo iv = new ExamResultVo.ExamResultIndexVo();
                iv.setId(idx.getId()); iv.setIndexName(idx.getIndexName());
                iv.setValue(idx.getValue()); iv.setReference(idx.getReference());
                iv.setIsAbnormal(idx.getIsAbnormal());
                return iv;
            }).collect(Collectors.toList()));
        }
        ExamResultImageText it = baseMapper.selectImageTextByResultId(vo.getId());
        if (it != null) {
            ExamResultVo.ExamResultImageTextVo itv = new ExamResultVo.ExamResultImageTextVo();
            itv.setId(it.getId()); itv.setCheckContent(it.getCheckContent());
            itv.setCheckConclusion(it.getCheckConclusion());
            vo.setImageText(itv);
        }
        if (vo.getApplyId() != null) {
            InspectOrder order = inspectOrderMapper.selectById(vo.getApplyId());
            if (order != null) {
                vo.setPatientName(order.getPatientName());
                Inspect inspect = inspectMapper.selectById(order.getInspectId());
                if (inspect != null) vo.setInspectName(inspect.getInspectName());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(ExamResultBo bo) {
        if (bo.getApplyId() == null) {
            throw new IllegalArgumentException("检查申请单不能为空");
        }
        InspectOrder order = inspectOrderMapper.selectById(bo.getApplyId());
        if (order == null) {
            throw new IllegalArgumentException("检查申请单不存在");
        }

        ExamResult entity = BeanUtil.toBean(bo, ExamResult.class);
        // 患者和挂号关系必须以检查申请单为准，防止前端空值或错误选择造成报告串线。
        entity.setPatientId(order.getPatientId());
        entity.setRegisterId(order.getRegisterId());
        entity.setCreateTime(new Date());
        entity.setReportTime(new Date());
        boolean ok = baseMapper.insert(entity) > 0;
        if (!ok) return false;
        bo.setId(entity.getId());

        if ("NUMERIC".equals(bo.getItemType()) && bo.getIndices() != null) {
            for (ExamResultBo.ExamResultIndexBo ib : bo.getIndices()) {
                ExamResultIndex idx = new ExamResultIndex();
                idx.setResultId(entity.getId());
                idx.setIndexName(ib.getIndexName());
                idx.setValue(ib.getValue());
                idx.setReference(ib.getReference());
                idx.setIsAbnormal(ib.getIsAbnormal());
                baseMapper.insertIndex(idx);
            }
        } else if ("IMAGE_TEXT".equals(bo.getItemType()) && bo.getImageText() != null) {
            ExamResultImageText it = new ExamResultImageText();
            it.setResultId(entity.getId());
            it.setCheckContent(bo.getImageText().getCheckContent());
            it.setCheckConclusion(bo.getImageText().getCheckConclusion());
            baseMapper.insertImageText(it);
        }

        if (bo.getApplyId() != null) {
            updateStatus(bo.getApplyId(), "已出结果");
        }

        publishExamCompletedEvent(entity);
        return true;
    }

    @Override
    public Boolean updateByBo(ExamResultBo bo) {
        ExamResult entity = BeanUtil.toBean(bo, ExamResult.class);
        entity.setUpdateTime(new Date());
        return baseMapper.updateById(entity) > 0;
    }

    @Override
    public Boolean deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            baseMapper.deleteIndicesByResultId(id);
            baseMapper.deleteImageTextByResultId(id);
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean updateStatus(Long applyId, String status) {
        try {
            jdbcTemplate.update("UPDATE inspect_order SET status = ? WHERE inspect_order_id = ?", status, applyId);
            return true;
        } catch (Exception e) {
            log.error("更新申请单状态失败: {}", e.getMessage());
            return false;
        }
    }

    private void publishExamCompletedEvent(ExamResult result) {
        try {
            InspectOrder order = inspectOrderMapper.selectById(result.getApplyId());
            Inspect inspect = order != null ? inspectMapper.selectById(order.getInspectId()) : null;
            String inspectName = inspect != null ? inspect.getInspectName() : "";
            String itemType = result.getItemType();

            Map<String, Object> data = new HashMap<>();
            data.put("resultId", result.getId());
            data.put("applyId", result.getApplyId());
            data.put("patientId", result.getPatientId());
            data.put("patientName", order != null ? order.getPatientName() : "");
            data.put("registerId", result.getRegisterId());
            data.put("inspectName", inspectName);
            data.put("itemType", itemType);
            data.put("summaryConclusion", result.getSummaryConclusion());
            data.put("doctorId", result.getDoctorId());
            data.put("reportTime", result.getReportTime() != null ? result.getReportTime().toString() : null);

            if ("NUMERIC".equals(itemType)) {
                List<ExamResultIndex> indices = baseMapper.selectIndicesByResultId(result.getId());
                List<Map<String, Object>> allList = new ArrayList<>();
                List<Map<String, Object>> abList = new ArrayList<>();
                if (indices != null) {
                    for (ExamResultIndex idx : indices) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("indexName", idx.getIndexName());
                        item.put("value", idx.getValue());
                        item.put("reference", idx.getReference());
                        item.put("isAbnormal", idx.getIsAbnormal());
                        allList.add(item);
                        if (idx.getIsAbnormal() != null && idx.getIsAbnormal() == 1) {
                            abList.add(item);
                        }
                    }
                }
                data.put("indicators", allList);
                data.put("abnormalIndicators", abList);
            } else if ("IMAGE_TEXT".equals(itemType)) {
                ExamResultImageText imageText = baseMapper.selectImageTextByResultId(result.getId());
                data.put("checkContent", imageText != null ? imageText.getCheckContent() : "");
                data.put("checkConclusion", imageText != null ? imageText.getCheckConclusion() : result.getSummaryConclusion());
            }

            Map<String, Object> envelope = new HashMap<>();
            envelope.put("eventId", "exam-completed-" + result.getId());
            envelope.put("eventType", "EXAM_COMPLETED");
            envelope.put("source", "his-system");
            envelope.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            envelope.put("data", data);

            rabbitTemplate.convertAndSend("hospital.events", "exam.completed", envelope);
            log.info("检查结果出炉事件已发布 - resultId: {}, patientId: {}, itemType: {}", result.getId(), result.getPatientId(), itemType);
        } catch (Exception e) {
            log.error("发布检查结果事件失败: {}", e.getMessage(), e);
        }
    }
}
