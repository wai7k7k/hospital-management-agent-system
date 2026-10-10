package xian.woniuxy.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import xian.woniuxy.customer.domain.bo.RecordBo;
import xian.woniuxy.customer.domain.vo.RecordVo;
import xian.woniuxy.customer.mapper.RecordMapper;
import xian.woniuxy.customer.service.IRecordService;
import xian.woniuxy.domain.Record;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 病历;病历Service业务层处理
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements IRecordService {

    private final RecordMapper baseMapper;

    /**
     * 查询病历;病历
     */
    @Override
    public RecordVo queryById(Long recordId){
        return baseMapper.selectVoById(recordId);
    }

    /**
     * 查询病历;病历列表
     */
    @Override
    public TableDataInfo<RecordVo> queryPageList(RecordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Record> lqw = buildQueryWrapper(bo);
        Page<RecordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询病历;病历列表
     */
    @Override
    public List<RecordVo> queryList(RecordBo bo) {
        LambdaQueryWrapper<Record> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Record> buildQueryWrapper(RecordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Record> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPatientId() != null, Record::getPatientId, bo.getPatientId());
        lqw.like(StringUtils.isNotBlank(bo.getPatientName()), Record::getPatientName, bo.getPatientName());
        lqw.eq(StringUtils.isNotBlank(bo.getRecordDesc()), Record::getRecordDesc, bo.getRecordDesc());
        lqw.eq(StringUtils.isNotBlank(bo.getRecordHealth()), Record::getRecordHealth, bo.getRecordHealth());
        lqw.eq(StringUtils.isNotBlank(bo.getRecordInfect()), Record::getRecordInfect, bo.getRecordInfect());
        lqw.eq(StringUtils.isNotBlank(bo.getRecordInfectDetails()), Record::getRecordInfectDetails, bo.getRecordInfectDetails());
        lqw.eq(StringUtils.isNotBlank(bo.getRecordAllergy()), Record::getRecordAllergy, bo.getRecordAllergy());
        lqw.eq(StringUtils.isNotBlank(bo.getRecordAllergyDetails()), Record::getRecordAllergyDetails, bo.getRecordAllergyDetails());
        lqw.eq(StringUtils.isNotBlank(bo.getRecordInherit()), Record::getRecordInherit, bo.getRecordInherit());
        lqw.eq(StringUtils.isNotBlank(bo.getRecordInheritDetails()), Record::getRecordInheritDetails, bo.getRecordInheritDetails());
        lqw.eq(bo.getRecordDate() != null, Record::getRecordDate, bo.getRecordDate());
        return lqw;
    }

    /**
     * 新增病历;病历
     */
    @Override
    public Boolean insertByBo(RecordBo bo) {
        Record add = BeanUtil.toBean(bo, Record.class);
        add.setRecordDate(new Date());
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setRecordId(add.getRecordId());
        }
        return flag;
    }

    /**
     * 修改病历;病历
     */
    @Override
    public Boolean updateByBo(RecordBo bo) {
        Record update = BeanUtil.toBean(bo, Record.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Record entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除病历;病历
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
