package xian.woniuxy.reghos.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import org.springframework.transaction.annotation.Transactional;
import xian.woniuxy.customer.dubboService.RemotePresService;
import xian.woniuxy.domain.Medicine;
import xian.woniuxy.domain.Operation;
import xian.woniuxy.reghos.domain.bo.OperationBo;
import xian.woniuxy.reghos.domain.vo.OperationVo;
import xian.woniuxy.reghos.mapper.DepMapper;
import xian.woniuxy.reghos.mapper.OperationMapper;
import xian.woniuxy.reghos.service.IOperationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 手术项目Service业务层处理
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class OperationServiceImpl implements IOperationService {

    private final OperationMapper baseMapper;
    @DubboReference
    private final RemoteDeptService remoteDeptService;

    /**
     * 查询手术项目
     */
    @Override
    public OperationVo queryById(Long operationId){

        return baseMapper.selectVoById(operationId);
    }

    /**
     * 查询手术项目列表
     */
    @Override
    public TableDataInfo<OperationVo> queryPageList(OperationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Operation> lqw = buildQueryWrapper(bo);
        Page<OperationVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<SysDept> sysDepts = remoteDeptService.deptList();
        HashMap<Long, String> map = new HashMap<>(16);
        sysDepts.forEach(dept -> {
            map.put(dept.getDeptId(), dept.getDeptName());
        });
        List<OperationVo> records = result.getRecords();
        records.forEach(operationVo -> {
            operationVo.setDepName(map.get(operationVo.getDepId()));
        });
        return TableDataInfo.build(result);
    }

    /**
     * 查询手术项目列表
     */
    @Override
    public List<OperationVo> queryList(OperationBo bo) {
        LambdaQueryWrapper<Operation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Operation> buildQueryWrapper(OperationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Operation> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getOperationName()), Operation::getOperationName, bo.getOperationName());
        lqw.like(StringUtils.isNotBlank(bo.getDepName()), Operation::getDepId, bo.getDepName());
        lqw.eq(true, Operation::getSoftDel, 1);
        return lqw;
    }

    /**
     * 新增手术项目
     */
    @Override
    public Boolean insertByBo(OperationBo bo) {
        Operation add = BeanUtil.toBean(bo, Operation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag ) {
            bo.setOperationId(add.getOperationId());
        }
        return flag;
    }

    /**
     * 修改手术项目
     */
    @Override
    public Boolean updateByBo(OperationBo bo) {
        Operation update = BeanUtil.toBean(bo, Operation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Operation entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除手术项目
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.updateByIdsoftDel(ids) > 0;
    }
}
