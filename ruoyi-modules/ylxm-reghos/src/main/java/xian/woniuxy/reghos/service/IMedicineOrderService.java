package xian.woniuxy.reghos.service;

import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import xian.woniuxy.domain.Medicine;
import xian.woniuxy.domain.MedicineOrder;
import xian.woniuxy.domain.Prescription;
import xian.woniuxy.reghos.domain.bo.MedicineOrderBo;
import xian.woniuxy.reghos.domain.vo.MedicineOrderVo;

import java.util.Collection;
import java.util.List;

/**
 * 药品订单Service接口
 *
 * @author ataoxz
 * @date 2022-08-23
 */
public interface IMedicineOrderService {

    /**
     * 查询药品订单
     */
    MedicineOrderVo queryById(Long moId);

    /**
     * 查询药品订单列表
     */
    TableDataInfo<MedicineOrderVo> queryPageList(MedicineOrderBo bo, PageQuery pageQuery);

    /**
     * 查询药品订单列表
     */
    List<MedicineOrderVo> queryList(MedicineOrderBo bo);

    /**
     * 修改药品订单
     */
    Boolean insertByBo(MedicineOrder bo);

    /**
     * 修改药品订单
     */
    Boolean updateByBo(MedicineOrderBo bo);

    /**
     * 校验并批量删除药品订单信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查询药品订单详情数据
     */
    MedicineOrderVo queryByIdDetails(Long moId);

    List<Prescription> getPres(Long registerId, String patientName);

    List<Medicine> getMedicines( String name);

    Boolean buyMed(List<MedicineOrder> list, Long id);
}
