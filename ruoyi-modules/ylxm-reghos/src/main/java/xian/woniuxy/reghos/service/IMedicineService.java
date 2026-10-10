package xian.woniuxy.reghos.service;

import xian.woniuxy.reghos.domain.vo.MedicineVo;
import xian.woniuxy.reghos.domain.bo.MedicineBo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 药品Service接口
 *
 * @author ataoxz
 * @date 2022-08-22
 */
public interface IMedicineService {

    /**
     * 查询药品
     */
    MedicineVo queryById(Long medicineId);

    /**
     * 查询药品列表
     */
    TableDataInfo<MedicineVo> queryPageList(MedicineBo bo, PageQuery pageQuery);

    /**
     * 查询药品列表
     */
    List<MedicineVo> queryList(MedicineBo bo);

    /**
     * 修改药品
     */
    Boolean insertByBo(MedicineBo bo);

    /**
     * 修改药品
     */
    Boolean updateByBo(MedicineBo bo);

    /**
     * 校验并批量删除药品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
