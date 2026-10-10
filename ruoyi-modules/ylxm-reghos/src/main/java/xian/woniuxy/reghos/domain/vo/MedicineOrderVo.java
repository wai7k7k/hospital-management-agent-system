package xian.woniuxy.reghos.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 药品订单视图对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class MedicineOrderVo {

    private static final long serialVersionUID = 1L;

    /**
     * 药品订单id
     */
    @ExcelProperty(value = "药品订单id")
    private Long moId;

    /**
     * 药品id
     */
    @ExcelProperty(value = "药品id")
    private Long medicineId;

    /**
     * 药品数量
     */
    @ExcelProperty(value = "药品数量")
    private Long medicineNum;

    /**
     * 药品单价
     */
    @ExcelProperty(value = "药品单价")
    private BigDecimal medicinePrice;

    /**
     * 订单总价
     */
    @ExcelProperty(value = "订单总价")
    private BigDecimal moTotalPrice;

    /**
     * 订单时间
     */
    @ExcelProperty(value = "订单时间")
    private Date moDate;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 处方id
     */
    @ExcelProperty(value = "处方id")
    private Long prescriptionId;


    /**
     * 患者名称
     */
    @ExcelProperty(value = "患者名称")
    private String patientName;

    /**
     * 医生名称
     */
    @ExcelProperty(value = "医生名称")
    private String nickName;

    /**
     * 药品名称
     */
    @ExcelProperty(value = "医生名称")
    private String medicineName;
}
