package xian.woniuxy.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.Date;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 药品订单对象 medicine_order
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("medicine_order")
public class MedicineOrder extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 药品订单id
     */
    @TableId(value = "mo_id")
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

//    /**
//     * 患者名称
//     */
//    private String patientName;
//
//    /**
//     * 医生名称
//     */
//    private String nickName;
}
