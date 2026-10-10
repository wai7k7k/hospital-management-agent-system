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
import xian.woniuxy.reghos.domain.bo.LftsBo;
import xian.woniuxy.reghos.domain.vo.LftsVo;
import xian.woniuxy.domain.Lfts;
import xian.woniuxy.reghos.event.ReportEventPublisher;
import xian.woniuxy.reghos.mapper.LftsMapper;
import xian.woniuxy.reghos.service.ILftsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;

/**
 * 肝功能结果Service业务层处理
 *
 * @author G
 * @date 2022-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class LftsServiceImpl implements ILftsService {

    private final LftsMapper baseMapper;
    private final ReportEventPublisher reportEventPublisher;

    /**
     * 查询肝功能结果
     */
    @Override
    public LftsVo queryById(Long lftsId){
        return baseMapper.selectVoById(lftsId);
    }

    /**
     * 查询肝功能结果列表
     */
    @Override
    public TableDataInfo<LftsVo> queryPageList(LftsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Lfts> lqw = buildQueryWrapper(bo);
        Page<LftsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询肝功能结果列表
     */
    @Override
    public List<LftsVo> queryList(LftsBo bo) {
        LambdaQueryWrapper<Lfts> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Lfts> buildQueryWrapper(LftsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Lfts> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPatientId() != null, Lfts::getPatientId, bo.getPatientId());
        lqw.like(StringUtils.isNotBlank(bo.getPatientName()), Lfts::getPatientName, bo.getPatientName());
        lqw.eq(bo.getRegisterId() != null, Lfts::getRegisterId, bo.getRegisterId());
        lqw.eq(bo.getALT() != null, Lfts::getALT, bo.getALT());
        lqw.eq(StringUtils.isNotBlank(bo.getALTReference()), Lfts::getALTReference, bo.getALTReference());
        lqw.eq(bo.getTP() != null, Lfts::getTP, bo.getTP());
        lqw.eq(StringUtils.isNotBlank(bo.getTPReference()), Lfts::getTPReference, bo.getTPReference());
        lqw.eq(bo.getAG() != null, Lfts::getAG, bo.getAG());
        lqw.eq(StringUtils.isNotBlank(bo.getAGReference()), Lfts::getAGReference, bo.getAGReference());
        lqw.eq(bo.getTBIL() != null, Lfts::getTBIL, bo.getTBIL());
        lqw.eq(StringUtils.isNotBlank(bo.getTBILReference()), Lfts::getTBILReference, bo.getTBILReference());
        lqw.eq(bo.getDBIL() != null, Lfts::getDBIL, bo.getDBIL());
        lqw.eq(StringUtils.isNotBlank(bo.getDBILReference()), Lfts::getDBILReference, bo.getDBILReference());
        return lqw;
    }

    /**
     * 新增肝功能结果
     */
    @Override
    public Boolean insertByBo(LftsBo bo) {
        Lfts add = BeanUtil.toBean(bo, Lfts.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setLftsId(add.getLftsId());
            publishLftsCompleted(add);
        }
        return flag;
    }

    /**
     * 修改肝功能结果
     */
    @Override
    public Boolean updateByBo(LftsBo bo) {
        Lfts update = BeanUtil.toBean(bo, Lfts.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Lfts entity){
        //TODO 做一些数据校验,如唯一约束
    }

    private void publishLftsCompleted(Lfts entity) {
        try {
            Map<String, String> indicators = new HashMap<>();
            indicators.put("ALT", entity.getALT() != null ? entity.getALT().toString() : "");
            indicators.put("TP", entity.getTP() != null ? entity.getTP().toString() : "");
            indicators.put("AG", entity.getAG() != null ? entity.getAG().toString() : "");
            indicators.put("TBIL", entity.getTBIL() != null ? entity.getTBIL().toString() : "");
            indicators.put("DBIL", entity.getDBIL() != null ? entity.getDBIL().toString() : "");

            Map<String, String> references = new HashMap<>();
            references.put("ALT", entity.getALTReference());
            references.put("TP", entity.getTPReference());
            references.put("AG", entity.getAGReference());
            references.put("TBIL", entity.getTBILReference());
            references.put("DBIL", entity.getDBILReference());

            reportEventPublisher.publishReportCompleted(
                    "lfts", "肝功能报告",
                    entity.getLftsId(), entity.getPatientId(), entity.getPatientName(),
                    entity.getRegisterId(), indicators, references);
        } catch (Exception e) {
            log.error("发布肝功能报告事件失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 批量删除肝功能结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
