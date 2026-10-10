package xian.woniuxy.reghos.service;

import xian.woniuxy.domain.Lfts;
import xian.woniuxy.reghos.domain.vo.LftsVo;
import xian.woniuxy.reghos.domain.bo.LftsBo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 肝功能结果Service接口
 *
 * @author G
 * @date 2022-08-23
 */
public interface ILftsService {

    /**
     * 查询肝功能结果
     */
    LftsVo queryById(Long lftsId);

    /**
     * 查询肝功能结果列表
     */
    TableDataInfo<LftsVo> queryPageList(LftsBo bo, PageQuery pageQuery);

    /**
     * 查询肝功能结果列表
     */
    List<LftsVo> queryList(LftsBo bo);

    /**
     * 修改肝功能结果
     */
    Boolean insertByBo(LftsBo bo);

    /**
     * 修改肝功能结果
     */
    Boolean updateByBo(LftsBo bo);

    /**
     * 校验并批量删除肝功能结果信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
