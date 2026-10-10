package xian.woniuxy.reghos.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xian.woniuxy.reghos.domain.bo.EquipmentBo;
import xian.woniuxy.reghos.domain.vo.EquipmentVo;
import xian.woniuxy.domain.Equipment;
import xian.woniuxy.reghos.mapper.EquipmentMapper;
import xian.woniuxy.reghos.service.IEquipmentService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 设备Service业务层处理
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class EquipmentServiceImpl implements IEquipmentService {

    private final EquipmentMapper baseMapper;

    /**
     * 查询设备
     */
    @Override
    public EquipmentVo queryById(Long equipmentId){
        return baseMapper.selectVoById(equipmentId);
    }

    /**
     * 查询设备列表
     */
    @Override
    public TableDataInfo<EquipmentVo> queryPageList(EquipmentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Equipment> lqw = buildQueryWrapper(bo);
        Page<EquipmentVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询设备列表
     */
    @Override
    public List<EquipmentVo> queryList(EquipmentBo bo) {
        LambdaQueryWrapper<Equipment> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Equipment> buildQueryWrapper(EquipmentBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Equipment> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getEquipmentNum()), Equipment::getEquipmentNum, bo.getEquipmentNum());
        lqw.like(StringUtils.isNotBlank(bo.getEquipmentName()), Equipment::getEquipmentName, bo.getEquipmentName());
        lqw.eq(bo.getEquipmentTime() != null, Equipment::getEquipmentTime, bo.getEquipmentTime());
        lqw.eq(bo.getSoftDel() != null, Equipment::getSoftDel, bo.getSoftDel());
        return lqw;
    }

    /**
     * 新增设备
     */
    @Override
    public Boolean insertByBo(EquipmentBo bo) {
        Equipment add = BeanUtil.toBean(bo, Equipment.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setEquipmentId(add.getEquipmentId());
        }
        return flag;
    }

    /**
     * 修改设备
     */
    @Override
    public Boolean updateByBo(EquipmentBo bo) {
        Equipment update = BeanUtil.toBean(bo, Equipment.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Equipment entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除设备
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
