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
 * 检查项目订单;视图对象
 *
 * @author G
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class InspectOrderVo {

    private static final long serialVersionUID = 1L;

    /**
     * 检查项目订单id
     */
    @ExcelProperty(value = "检查项目订单id")
    private Long inspectOrderId;

    /**
     * 病人id
     */
    @ExcelProperty(value = "病人id")
    private Long patientId;

    /**
     * 开单医师id
     */
    @ExcelProperty(value = "开单医师id")
    private Long userId;


    /**
     * 开单医师name
     */
    @ExcelProperty(value = "开单医师id")
    private String userName;

    /**
     * 挂号id
     */
    @ExcelProperty(value = "挂号id")
    private Long registerId;

    /**
     * 住院id
     */
    @ExcelProperty(value = "住院id")
    private Long enterId;

    /**
     * 检查项目id
     */
    @ExcelProperty(value = "检查项目id")
    private Long inspectId;

    /**
     * 检查项目Name
     */
    @ExcelProperty(value = "检查项目id")
    private String inspectName;

    /**
     * 检查项目时间
     */
    @ExcelProperty(value = "检查项目时间")
    private Date inspectOrderDate;

    /**
     * 检查项目价格
     */
    @ExcelProperty(value = "检查项目价格")
    private BigDecimal inspectOrderPrice;

    /**
     * 病人姓名
     */
    @ExcelProperty(value = "病人姓名")
    private String patientName;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
