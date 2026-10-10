package xian.woniuxy.reghos.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.system.api.RemoteDeptService;
import com.ruoyi.system.api.domain.SysDept;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import xian.woniuxy.domain.Bed;
import xian.woniuxy.reghos.domain.bo.BedBo;
import xian.woniuxy.reghos.domain.vo.BedVo;

import xian.woniuxy.reghos.mapper.BedMapper;
import xian.woniuxy.reghos.service.IBedService;

import java.rmi.Remote;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 病床床位;Service业务层处理
 *
 * @author G
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class BedServiceImpl implements IBedService {

    private final BedMapper baseMapper;
    @DubboReference
    private final RemoteDeptService remoteDeptService;

    /**
     * 查询病床床位;
     */
    @Override
    public BedVo queryById(Long bedId){
        return baseMapper.selectVoById(bedId);
    }

    /**
     * 查询病床床位;列表
     */
    @Override
    public TableDataInfo<BedVo> queryPageList(BedBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Bed> lqw = buildQueryWrapper(bo);
        Page<BedVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<SysDept> sysDepts = remoteDeptService.deptList();
        HashMap<Long, String> map = new HashMap<>();
        sysDepts.forEach(dept -> {
            map.put(dept.getDeptId(),dept.getDeptName());
        });
        List<BedVo> records = result.getRecords();
        records.forEach(bedVo -> {
            bedVo.setDepName(map.get(bedVo.getDepId()));
        });
        return TableDataInfo.build(result);
    }

    /**
     * 查询病床床位;列表
     */
    @Override
    public List<BedVo> queryList(BedBo bo) {
        LambdaQueryWrapper<Bed> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Bed> buildQueryWrapper(BedBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Bed> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getBedNum()), Bed::getBedNum, bo.getBedNum());
        lqw.eq(bo.getDepId() != null, Bed::getDepId, bo.getDepId());
        lqw.eq(bo.getBedState() != null, Bed::getBedState, bo.getBedState());
        lqw.eq(bo.getSoftDel() != null, Bed::getSoftDel, bo.getSoftDel());
        return lqw;
    }

    /**
     * 新增病床床位;
     */
    @Override
    public Boolean insertByBo(BedBo bo) {
        Bed add = BeanUtil.toBean(bo, Bed.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setBedId(add.getBedId());
        }
        return flag;
    }

    /**
     * 修改病床床位;
     */
    @Override
    public Boolean updateByBo(BedBo bo) {
        Bed update = BeanUtil.toBean(bo, Bed.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Bed entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除病床床位;
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<Bed> getBedListByDepId(Long depId) {
        QueryWrapper<Bed> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dep_id", depId);
        queryWrapper.eq("bed_state", 0);
        return baseMapper.selectList(queryWrapper);
    }
}
