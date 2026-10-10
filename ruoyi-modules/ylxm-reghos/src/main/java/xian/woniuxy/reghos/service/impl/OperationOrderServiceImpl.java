package xian.woniuxy.reghos.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysUser;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import xian.woniuxy.domain.OperationOrder;
import xian.woniuxy.reghos.domain.bo.OperationOrderBo;
import xian.woniuxy.reghos.domain.vo.OperationOrderVo;

import xian.woniuxy.reghos.mapper.OperationOrderMapper;
import xian.woniuxy.reghos.service.IOperationOrderService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 手术订单;Service业务层处理
 *
 * @author G
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class OperationOrderServiceImpl implements IOperationOrderService {

    private final OperationOrderMapper baseMapper;

    @DubboReference
    private RemoteUserService remoteUserService;


    /**
     * 查询手术订单;
     */
    @Override
    public OperationOrderVo queryById(Long ooId){
        return baseMapper.selectVoById(ooId);
    }

    /**
     * 查询医生名字列表;
     */
    @Override
    public List<SysUser> queryAllUserName(){
        return remoteUserService.queryAllUserName();
    }

    /**
     * 查询手术订单;列表
     */
    @Override
    public TableDataInfo<OperationOrderVo> queryPageList(OperationOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OperationOrder> lqw = buildQueryWrapper(bo);
        Page<OperationOrderVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询手术订单;列表
     */
    @Override
    public List<OperationOrderVo> queryList(OperationOrderBo bo) {
        LambdaQueryWrapper<OperationOrder> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<OperationOrder> buildQueryWrapper(OperationOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<OperationOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getOperationId() != null, OperationOrder::getOperationId, bo.getOperationId());
        lqw.eq(bo.getUserId() != null, OperationOrder::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getUserName()), OperationOrder::getUserName, bo.getUserName());
        lqw.eq(bo.getOperationPrice() != null, OperationOrder::getOperationPrice, bo.getOperationPrice());
        lqw.eq(bo.getOoDate() != null, OperationOrder::getOoDate, bo.getOoDate());
        lqw.eq(bo.getOperationDate() != null, OperationOrder::getOperationDate, bo.getOperationDate());
        lqw.eq(StringUtils.isNotBlank(bo.getOpertationConfirm()), OperationOrder::getOpertationConfirm, bo.getOpertationConfirm());
        lqw.eq(bo.getRegisterId() != null, OperationOrder::getRegisterId, bo.getRegisterId());
        lqw.eq(bo.getEnterId() != null, OperationOrder::getEnterId, bo.getEnterId());
        return lqw;
    }

    /**
     * 新增手术订单;
     */
    @Override
    public Boolean insertByBo(OperationOrderBo bo) {
        OperationOrder add = BeanUtil.toBean(bo, OperationOrder.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setOoId(add.getOoId());
        }
        return flag;
    }

    /**
     * 修改手术订单;
     */
    @Override
    public Boolean updateByBo(OperationOrderBo bo) {
        OperationOrder update = BeanUtil.toBean(bo, OperationOrder.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(OperationOrder entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除手术订单;
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
