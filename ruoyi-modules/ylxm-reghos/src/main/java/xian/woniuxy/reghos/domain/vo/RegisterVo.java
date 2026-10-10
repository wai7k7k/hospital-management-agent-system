package xian.woniuxy.reghos.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 挂号视图对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class RegisterVo {

    private static final long serialVersionUID = 1L;

    /**
     * 挂号id
     */
    @ExcelProperty(value = "挂号id")
    private Long registerId;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private Long patientId;

    /**
     * 用户名称
     */
   @TableField(exist = false)
    private String patientName;

    /**
     * 医生id
     */
    @ExcelProperty(value = "医生id")
    private Long userId;

    /**
     * 医生姓名
     */
    @ExcelProperty(value = "医生姓名")
    private String userName;

    /**
     * 挂号时间
     */
    @ExcelProperty(value = "挂号时间")
    private Date registerDate;

    /**
     * 科室id
     */
    @ExcelProperty(value = "科室id")
    private Long depId;

    /**
     * 科室名称
     */
    @ExcelProperty(value = "科室名称")
    private String depName;

    /**
     * 挂号价格
     */
    @ExcelProperty(value = "挂号价格")
    private BigDecimal registerPrice;

    /**
     * 是否完成0未完成，1已完成
     */
    @ExcelProperty(value = "是否完成0未完成，1已完成")
    private String registerState;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
