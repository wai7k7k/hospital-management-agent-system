package xian.woniuxy.customer.service;

import xian.woniuxy.customer.domain.bo.RecordBo;
import xian.woniuxy.customer.domain.vo.RecordVo;
import xian.woniuxy.domain.Record;

import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 病历;病历Service接口
 *
 * @author ataoxz
 * @date 2022-08-23
 */
public interface IRecordService {

    /**
     * 查询病历;病历
     */
    RecordVo queryById(Long recordId);

    /**
     * 查询病历;病历列表
     */
    TableDataInfo<RecordVo> queryPageList(RecordBo bo, PageQuery pageQuery);

    /**
     * 查询病历;病历列表
     */
    List<RecordVo> queryList(RecordBo bo);

    /**
     * 修改病历;病历
     */
    Boolean insertByBo(RecordBo bo);

    /**
     * 修改病历;病历
     */
    Boolean updateByBo(RecordBo bo);

    /**
     * 校验并批量删除病历;病历信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
