package xian.woniuxy.reghos.service;

import xian.woniuxy.reghos.domain.vo.InspectOrderVo;
import xian.woniuxy.reghos.domain.bo.InspectOrderBo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 检查项目订单;Service接口
 *
 * @author G
 * @date 2022-08-23
 */
public interface IInspectOrderService {

    /**
     * 查询检查项目订单;
     */
    InspectOrderVo queryById(Long inspectOrderId);

    /**
     * 查询检查项目订单;列表
     */
    TableDataInfo<InspectOrderVo> queryPageList(InspectOrderBo bo, PageQuery pageQuery);

    /**
     * 查询检查项目订单;列表
     */
    List<InspectOrderVo> queryList(InspectOrderBo bo);

    /**
     * 修改检查项目订单;
     */
    Boolean insertByBo(InspectOrderBo bo);

    /**
     * 修改检查项目订单;
     */
    Boolean updateByBo(InspectOrderBo bo);

    /**
     * 校验并批量删除检查项目订单;信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
