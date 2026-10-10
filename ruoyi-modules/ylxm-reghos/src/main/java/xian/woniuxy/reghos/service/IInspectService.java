package xian.woniuxy.reghos.service;

import xian.woniuxy.domain.Inspect;
import xian.woniuxy.reghos.domain.vo.InspectVo;
import xian.woniuxy.reghos.domain.bo.InspectBo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 检查项目;Service接口
 *
 * @author G
 * @date 2022-08-23
 */
public interface IInspectService {

    /**
     * 查询检查项目;
     */
    InspectVo queryById(Long inspectId);

    /**
     * 查询检查项目;列表
     */
    TableDataInfo<InspectVo> queryPageList(InspectBo bo, PageQuery pageQuery);

    /**
     * 查询检查项目;列表
     */
    List<InspectVo> queryList(InspectBo bo);

    /**
     * 修改检查项目;
     */
    Boolean insertByBo(InspectBo bo);

    /**
     * 修改检查项目;
     */
    Boolean updateByBo(InspectBo bo);

    /**
     * 校验并批量删除检查项目;信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
