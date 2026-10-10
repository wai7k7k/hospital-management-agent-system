package xian.woniuxy.reghos.service;

import xian.woniuxy.reghos.domain.vo.BloodRoutineVo;
import xian.woniuxy.reghos.domain.bo.BloodRoutineBo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 血常规结果;Service接口
 *
 * @author G
 * @date 2022-08-23
 */
public interface IBloodRoutineService {

    /**
     * 查询血常规结果;
     */
    BloodRoutineVo queryById(Long brId);

    /**
     * 查询血常规结果;列表
     */
    TableDataInfo<BloodRoutineVo> queryPageList(BloodRoutineBo bo, PageQuery pageQuery);

    /**
     * 查询血常规结果;列表
     */
    List<BloodRoutineVo> queryList(BloodRoutineBo bo);

    /**
     * 修改血常规结果;
     */
    Boolean insertByBo(BloodRoutineBo bo);

    /**
     * 修改血常规结果;
     */
    Boolean updateByBo(BloodRoutineBo bo);

    /**
     * 校验并批量删除血常规结果;信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
