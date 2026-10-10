package xian.woniuxy.reghos.service;

import com.ruoyi.system.api.domain.SysUser;
import xian.woniuxy.domain.OperationOrder;
import xian.woniuxy.reghos.domain.vo.OperationOrderVo;
import xian.woniuxy.reghos.domain.bo.OperationOrderBo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 手术订单;Service接口
 *
 * @author G
 * @date 2022-08-23
 */
public interface IOperationOrderService {

    /**
     * 查询手术订单;
     */
    OperationOrderVo queryById(Long ooId);

    /**
     * 查询手术订单;列表
     */
    TableDataInfo<OperationOrderVo> queryPageList(OperationOrderBo bo, PageQuery pageQuery);

    /**
     * 查询手术订单;列表
     */
    List<OperationOrderVo> queryList(OperationOrderBo bo);

    /**
     * 修改手术订单;
     */
    Boolean insertByBo(OperationOrderBo bo);

    /**
     * 修改手术订单;
     */
    Boolean updateByBo(OperationOrderBo bo);

    /**
     * 校验并批量删除手术订单;信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<SysUser> queryAllUserName();
}
