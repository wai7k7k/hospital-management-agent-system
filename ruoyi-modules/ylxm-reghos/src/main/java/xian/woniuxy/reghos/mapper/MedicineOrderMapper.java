package xian.woniuxy.reghos.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import xian.woniuxy.domain.MedicineOrder;
import xian.woniuxy.reghos.domain.vo.MedicineOrderVo;

/**
 * 药品订单Mapper接口
 *
 * @author ataoxz
 * @date 2022-08-23
 */
public interface MedicineOrderMapper extends BaseMapperPlus<MedicineOrderMapper, MedicineOrder, MedicineOrderVo> {

    MedicineOrderVo queryByIdDetails(Long moId);

}
