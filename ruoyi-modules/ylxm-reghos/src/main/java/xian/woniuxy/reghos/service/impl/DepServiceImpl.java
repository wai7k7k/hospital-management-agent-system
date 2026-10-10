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
import xian.woniuxy.domain.Dep;
import xian.woniuxy.reghos.domain.bo.DepBo;
import xian.woniuxy.reghos.domain.vo.DepVo;
import xian.woniuxy.reghos.mapper.DepMapper;
import xian.woniuxy.reghos.service.IDepService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 科室;Service业务层处理
 *
 * @author G
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class DepServiceImpl implements IDepService {

    private final DepMapper baseMapper;

    /**
     * 查询科室;
     */
    @Override
    public DepVo queryById(Long depId){
        return baseMapper.selectVoById(depId);
    }

    /**
     * 查询科室;列表
     */
    @Override
    public TableDataInfo<DepVo> queryPageList(DepBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Dep> lqw = buildQueryWrapper(bo);
        Page<DepVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询科室;列表
     */
    @Override
    public List<DepVo> queryList(DepBo bo) {
        LambdaQueryWrapper<Dep> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Dep> buildQueryWrapper(DepBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Dep> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getDepName()), Dep::getDepName, bo.getDepName());
        lqw.eq(bo.getSoftDel() != null, Dep::getSoftDel, bo.getSoftDel());
        return lqw;
    }

    /**
     * 新增科室;
     */
    @Override
    public Boolean insertByBo(DepBo bo) {
        Dep add = BeanUtil.toBean(bo, Dep.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setDepId(add.getDepId());
        }
        return flag;
    }

    /**
     * 修改科室;
     */
    @Override
    public Boolean updateByBo(DepBo bo) {
        Dep update = BeanUtil.toBean(bo, Dep.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Dep entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除科室;
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
