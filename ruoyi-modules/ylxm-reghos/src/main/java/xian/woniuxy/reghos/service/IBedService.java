package xian.woniuxy.reghos.service;


import xian.woniuxy.domain.Bed;
import xian.woniuxy.reghos.domain.vo.BedVo;
import xian.woniuxy.reghos.domain.bo.BedBo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 病床床位;Service接口
 *
 * @author G
 * @date 2022-08-23
 */
public interface IBedService {

    /**
     * 查询病床床位;
     */
    BedVo queryById(Long bedId);

    /**
     * 查询病床床位;列表
     */
    TableDataInfo<BedVo> queryPageList(BedBo bo, PageQuery pageQuery);

    /**
     * 查询病床床位;列表
     */
    List<BedVo> queryList(BedBo bo);

    /**
     * 修改病床床位;
     */
    Boolean insertByBo(BedBo bo);

    /**
     * 修改病床床位;
     */
    Boolean updateByBo(BedBo bo);

    /**
     * 校验并批量删除病床床位;信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<Bed> getBedListByDepId(Long depId);
}
