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
import xian.woniuxy.reghos.domain.bo.InspectBo;
import xian.woniuxy.reghos.domain.vo.InspectVo;
import xian.woniuxy.domain.Inspect;
import xian.woniuxy.reghos.mapper.InspectMapper;
import xian.woniuxy.reghos.service.IInspectService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 检查项目;Service业务层处理
 *
 * @author G
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class InspectServiceImpl implements IInspectService {

    private final InspectMapper baseMapper;

    /**
     * 查询检查项目;
     */
    @Override
    public InspectVo queryById(Long inspectId){
        return baseMapper.selectVoById(inspectId);
    }

    /**
     * 查询检查项目;列表
     */
    @Override
    public TableDataInfo<InspectVo> queryPageList(InspectBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Inspect> lqw = buildQueryWrapper(bo);
        Page<InspectVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询检查项目;列表
     */
    @Override
    public List<InspectVo> queryList(InspectBo bo) {
        LambdaQueryWrapper<Inspect> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Inspect> buildQueryWrapper(InspectBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Inspect> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getInspectName()), Inspect::getInspectName, bo.getInspectName());
        lqw.like(StringUtils.isNotBlank(bo.getInspectTableName()), Inspect::getInspectTableName, bo.getInspectTableName());
        lqw.eq(bo.getInspectPrice() != null, Inspect::getInspectPrice, bo.getInspectPrice());
        lqw.eq(bo.getSoftDel() != null, Inspect::getSoftDel, bo.getSoftDel());
        return lqw;
    }

    /**
     * 新增检查项目;
     */
    @Override
    public Boolean insertByBo(InspectBo bo) {
        Inspect add = BeanUtil.toBean(bo, Inspect.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setInspectId(add.getInspectId());
        }
        return flag;
    }

    /**
     * 修改检查项目;
     */
    @Override
    public Boolean updateByBo(InspectBo bo) {
        Inspect update = BeanUtil.toBean(bo, Inspect.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Inspect entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除检查项目;
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
