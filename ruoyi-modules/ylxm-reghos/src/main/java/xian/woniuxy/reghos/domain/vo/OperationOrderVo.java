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
 * 手术订单;视图对象
 *
 * @author G
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class OperationOrderVo {

    private static final long serialVersionUID = 1L;

    /**
     * 手术订单id
     */
    @ExcelProperty(value = "手术订单id")
    private Long ooId;

    /**
     * 手术项目id
     */
    @ExcelProperty(value = "手术项目id")
    private Long operationId;

    /**
     * 主刀医生
     */
    @ExcelProperty(value = "主刀医生")
    private Long userId;

    /**
     * 主刀医生姓名
     */
    @ExcelProperty(value = "主刀医生姓名")
    private String userName;

    /**
     * 手术价格
     */
    @ExcelProperty(value = "手术价格")
    private BigDecimal operationPrice;

    /**
     * 手术订单时间
     */
    @ExcelProperty(value = "手术订单时间")
    private Date ooDate;

    /**
     * 手术时间
     */
    @ExcelProperty(value = "手术时间")
    private Date operationDate;

    /**
     * 家属是否已签字：是/否
     */
    @ExcelProperty(value = "家属是否已签字：是/否")
    private String opertationConfirm;

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
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
