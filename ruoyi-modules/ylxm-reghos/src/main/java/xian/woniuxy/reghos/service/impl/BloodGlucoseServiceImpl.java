package xian.woniuxy.reghos.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xian.woniuxy.domain.BloodGlucose;
import xian.woniuxy.reghos.domain.bo.BloodGlucoseBo;
import xian.woniuxy.reghos.domain.vo.BloodGlucoseVo;
import xian.woniuxy.reghos.event.ReportEventPublisher;
import xian.woniuxy.reghos.mapper.BloodGlucoseMapper;
import xian.woniuxy.reghos.service.IBloodGlucoseService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;

/**
 * 血糖结果;Service业务层处理
 *
 * @author G
 * @date 2022-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BloodGlucoseServiceImpl implements IBloodGlucoseService {

    private final BloodGlucoseMapper baseMapper;
    private final ReportEventPublisher reportEventPublisher;

    /**
     * 查询血糖结果;
     */
    @Override
    public BloodGlucoseVo queryById(Long bgId){
        return baseMapper.selectVoById(bgId);
    }

    /**
     * 查询血糖结果;列表
     */
    @Override
    public TableDataInfo<BloodGlucoseVo> queryPageList(BloodGlucoseBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BloodGlucose> lqw = buildQueryWrapper(bo);
        Page<BloodGlucoseVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询血糖结果;列表
     */
    @Override
    public List<BloodGlucoseVo> queryList(BloodGlucoseBo bo) {
        LambdaQueryWrapper<BloodGlucose> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BloodGlucose> buildQueryWrapper(BloodGlucoseBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<BloodGlucose> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPatientId() != null, BloodGlucose::getPatientId, bo.getPatientId());
        lqw.like(StringUtils.isNotBlank(bo.getPatientName()), BloodGlucose::getPatientName, bo.getPatientName());
        lqw.eq(bo.getRegisterId() != null, BloodGlucose::getRegisterId, bo.getRegisterId());
        lqw.eq(StringUtils.isNotBlank(bo.getGLU()), BloodGlucose::getGLU, bo.getGLU());
        lqw.eq(StringUtils.isNotBlank(bo.getGluReference()), BloodGlucose::getGluReference, bo.getGluReference());
        lqw.eq(StringUtils.isNotBlank(bo.getPH()), BloodGlucose::getPH, bo.getPH());
        lqw.eq(StringUtils.isNotBlank(bo.getPhReference()), BloodGlucose::getPhReference, bo.getPhReference());
        return lqw;
    }

    /**
     * 新增血糖结果;
     */
    @Override
    public Boolean insertByBo(BloodGlucoseBo bo) {
        BloodGlucose add = BeanUtil.toBean(bo, BloodGlucose.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setBgId(add.getBgId());
            publishBloodGlucoseCompleted(add);
        }
        return flag;
    }

    /**
     * 修改血糖结果;
     */
    @Override
    public Boolean updateByBo(BloodGlucoseBo bo) {
        BloodGlucose update = BeanUtil.toBean(bo, BloodGlucose.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BloodGlucose entity){
        //TODO 做一些数据校验,如唯一约束
    }

    private void publishBloodGlucoseCompleted(BloodGlucose entity) {
        try {
            Map<String, String> indicators = new HashMap<>();
            indicators.put("GLU", entity.getGLU());
            indicators.put("PH", entity.getPH());

            Map<String, String> references = new HashMap<>();
            references.put("GLU", entity.getGluReference());
            references.put("PH", entity.getPhReference());

            reportEventPublisher.publishReportCompleted(
                    "blood_glucose", "血糖报告",
                    entity.getBgId(), entity.getPatientId(), entity.getPatientName(),
                    entity.getRegisterId(), indicators, references);
        } catch (Exception e) {
            log.error("发布血糖报告事件失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 批量删除血糖结果;
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
