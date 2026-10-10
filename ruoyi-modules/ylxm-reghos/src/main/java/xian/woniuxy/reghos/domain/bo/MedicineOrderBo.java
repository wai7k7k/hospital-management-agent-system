package xian.woniuxy.reghos.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.math.BigDecimal;
import java.util.Date;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 药品订单业务对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class MedicineOrderBo extends BaseEntity {

    /**
     * 药品订单id
     */
    private Long moId;

    /**
     * 药品id
     */
    private Long medicineId;

    /**
     * 药品数量
     */
    private Long medicineNum;

    /**
     * 药品单价
     */
    private BigDecimal medicinePrice;

    /**
     * 订单总价
     */
    private BigDecimal moTotalPrice;

    /**
     * 订单时间
     */
    private Date moDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 处方id
     */
    private Long prescriptionId;


}
