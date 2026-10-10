package xian.woniuxy.reghos.service;

import xian.woniuxy.reghos.domain.vo.DepVo;
import xian.woniuxy.reghos.domain.bo.DepBo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 科室;Service接口
 *
 * @author G
 * @date 2022-08-23
 */
public interface IDepService {

    /**
     * 查询科室;
     */
    DepVo queryById(Long depId);

    /**
     * 查询科室;列表
     */
    TableDataInfo<DepVo> queryPageList(DepBo bo, PageQuery pageQuery);

    /**
     * 查询科室;列表
     */
    List<DepVo> queryList(DepBo bo);

    /**
     * 修改科室;
     */
    Boolean insertByBo(DepBo bo);

    /**
     * 修改科室;
     */
    Boolean updateByBo(DepBo bo);

    /**
     * 校验并批量删除科室;信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
