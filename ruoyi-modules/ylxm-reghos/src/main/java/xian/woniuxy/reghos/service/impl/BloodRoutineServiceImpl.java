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
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import xian.woniuxy.customer.dubboService.RemotePatientService;
import xian.woniuxy.reghos.domain.bo.BloodRoutineBo;
import xian.woniuxy.reghos.domain.vo.BloodRoutineVo;
import xian.woniuxy.domain.BloodRoutine;
import xian.woniuxy.reghos.event.ReportEventPublisher;
import xian.woniuxy.reghos.mapper.BloodRoutineMapper;
import xian.woniuxy.reghos.service.IBloodRoutineService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;

/**
 * 血常规结果;Service业务层处理
 *
 * @author G
 * @date 2022-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BloodRoutineServiceImpl implements IBloodRoutineService {

    private final BloodRoutineMapper baseMapper;
    private final ReportEventPublisher reportEventPublisher;

    @DubboReference
    private RemotePatientService remotePatientService;
    /**
     * 查询血常规结果;
     */
    @Override
    public BloodRoutineVo queryById(Long brId){
        return baseMapper.selectVoById(brId);
    }

    /**
     * 查询血常规结果;列表
     */
    @Override
    public TableDataInfo<BloodRoutineVo> queryPageList(BloodRoutineBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BloodRoutine> lqw = buildQueryWrapper(bo);
        Page<BloodRoutineVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询血常规结果;列表
     */
    @Override
    public List<BloodRoutineVo> queryList(BloodRoutineBo bo) {
        LambdaQueryWrapper<BloodRoutine> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BloodRoutine> buildQueryWrapper(BloodRoutineBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<BloodRoutine> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPatientId() != null, BloodRoutine::getPatientId, bo.getPatientId());
        lqw.like(StringUtils.isNotBlank(bo.getPatientName()), BloodRoutine::getPatientName, bo.getPatientName());
        lqw.eq(bo.getRegisterId() != null, BloodRoutine::getRegisterId, bo.getRegisterId());
        lqw.eq(StringUtils.isNotBlank(bo.getWbc()), BloodRoutine::getWBC, bo.getWbc());
        lqw.eq(StringUtils.isNotBlank(bo.getWbcReference()), BloodRoutine::getWbcReference, bo.getWbcReference());
        lqw.eq(StringUtils.isNotBlank(bo.getRbc()), BloodRoutine::getRBC, bo.getRbc());
        lqw.eq(StringUtils.isNotBlank(bo.getRbcReference()), BloodRoutine::getRbcReference, bo.getRbcReference());
        return lqw;
    }

    /**
     * 新增血常规结果;
     */
    @Override
    public Boolean insertByBo(BloodRoutineBo bo) {
        BloodRoutine add = BeanUtil.toBean(bo, BloodRoutine.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setBrId(add.getBrId());
            publishBloodRoutineCompleted(add);
        }
        return flag;
    }

    /**
     * 修改血常规结果;
     */
    @Override
    public Boolean updateByBo(BloodRoutineBo bo) {
        BloodRoutine update = BeanUtil.toBean(bo, BloodRoutine.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BloodRoutine entity){
        //TODO 做一些数据校验,如唯一约束
    }

    private void publishBloodRoutineCompleted(BloodRoutine entity) {
        try {
            Map<String, String> indicators = new HashMap<>();
            indicators.put("WBC", entity.getWBC());
            indicators.put("RBC", entity.getRBC());

            Map<String, String> references = new HashMap<>();
            references.put("WBC", entity.getWbcReference());
            references.put("RBC", entity.getRbcReference());

            reportEventPublisher.publishReportCompleted(
                    "blood_routine", "血常规报告",
                    entity.getBrId(), entity.getPatientId(), entity.getPatientName(),
                    entity.getRegisterId(), indicators, references);
        } catch (Exception e) {
            log.error("发布血常规报告事件失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 批量删除血常规结果;
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
