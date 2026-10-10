package xian.woniuxy.reghos.service;

import xian.woniuxy.reghos.domain.vo.OperationVo;
import xian.woniuxy.reghos.domain.bo.OperationBo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 手术项目Service接口
 *
 * @author ataoxz
 * @date 2022-08-23
 */
public interface IOperationService {

    /**
     * 查询手术项目
     */
    OperationVo queryById(Long operationId);

    /**
     * 查询手术项目列表
     */
    TableDataInfo<OperationVo> queryPageList(OperationBo bo, PageQuery pageQuery);

    /**
     * 查询手术项目列表
     */
    List<OperationVo> queryList(OperationBo bo);

    /**
     * 修改手术项目
     */
    Boolean insertByBo(OperationBo bo);

    /**
     * 修改手术项目
     */
    Boolean updateByBo(OperationBo bo);

    /**
     * 校验并批量删除手术项目信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
