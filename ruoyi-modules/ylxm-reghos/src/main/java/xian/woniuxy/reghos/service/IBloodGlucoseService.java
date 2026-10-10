package xian.woniuxy.reghos.service;

import xian.woniuxy.reghos.domain.vo.BloodGlucoseVo;
import xian.woniuxy.reghos.domain.bo.BloodGlucoseBo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 血糖结果;Service接口
 *
 * @author G
 * @date 2022-08-23
 */
public interface IBloodGlucoseService {

    /**
     * 查询血糖结果;
     */
    BloodGlucoseVo queryById(Long bgId);

    /**
     * 查询血糖结果;列表
     */
    TableDataInfo<BloodGlucoseVo> queryPageList(BloodGlucoseBo bo, PageQuery pageQuery);

    /**
     * 查询血糖结果;列表
     */
    List<BloodGlucoseVo> queryList(BloodGlucoseBo bo);

    /**
     * 修改血糖结果;
     */
    Boolean insertByBo(BloodGlucoseBo bo);

    /**
     * 修改血糖结果;
     */
    Boolean updateByBo(BloodGlucoseBo bo);

    /**
     * 校验并批量删除血糖结果;信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
