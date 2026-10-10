package xian.woniuxy.customer.service;


import xian.woniuxy.domain.Enter;
import xian.woniuxy.domain.vo.EnterVo;
import xian.woniuxy.domain.bo.EnterBo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 住院;Service接口
 *
 * @author ataoxz
 * @date 2022-08-23
 */
public interface IEnterService {

    /**
     * 查询住院;
     */
    EnterVo queryById(Long enterId);

    /**
     * 查询住院;列表
     */
    TableDataInfo<EnterVo> queryPageList(EnterBo bo, PageQuery pageQuery);

    /**
     * 查询住院;列表
     */
    List<EnterVo> queryList(EnterBo bo);

    /**
     * 修改住院;
     */
    Boolean insertByBo(EnterBo bo);

    /**
     * 修改住院;
     */
    Boolean updateByBo(EnterBo bo);

    /**
     * 校验并批量删除住院;信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    String goZhuYuan(Long registerId);

    Enter goFBed(Enter enter);

    Enter goChuYuan(Enter enter);
}
