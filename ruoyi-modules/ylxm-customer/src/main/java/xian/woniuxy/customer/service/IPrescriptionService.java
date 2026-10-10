package xian.woniuxy.customer.service;

import xian.woniuxy.customer.domain.bo.PrescriptionBo;
import xian.woniuxy.customer.domain.vo.PrescriptionVo;
import xian.woniuxy.domain.Prescription;

import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 处方Service接口
 *
 * @author ataoxz
 * @date 2022-08-23
 */
public interface IPrescriptionService {

    /**
     * 查询处方
     */
    PrescriptionVo queryById(Long prescriptionId);

    /**
     * 查询处方列表
     */
    TableDataInfo<PrescriptionVo> queryPageList(PrescriptionBo bo, PageQuery pageQuery);

    /**
     * 查询处方列表
     */
    List<PrescriptionVo> queryList(PrescriptionBo bo);

    /**
     * 新增处方
     */
    Boolean insertByBo(PrescriptionBo bo);

    /**
     * 修改处方
     */
    Boolean updateByBo(PrescriptionBo bo);

    /**
     * 校验并批量删除处方信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
