package xian.woniuxy.customer.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 用户视图对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class PatientVo {

    private static final long serialVersionUID = 1L;

    /**
     * 病人唯一id
     */
    @ExcelProperty(value = "病人唯一id")
    private Long patientId;

    /**
     * 手机号/账号
     */
    @ExcelProperty(value = "手机号/账号")
    private String patientPhone;

    /**
     * 密码
     */
    @ExcelProperty(value = "密码")
    private String patientPassword;

    /**
     * 名字
     */
    @ExcelProperty(value = "名字")
    private String patientName;

    /**
     * 性别
     */
    @ExcelProperty(value = "性别")
    private String patientSex;

    /**
     * 身份证
     */
    @ExcelProperty(value = "身份证")
    private String patientCardId;

    /**
     * 头像
     */
    @ExcelProperty(value = "头像")
    private String patientPhoto;

    /**
     * 生日年龄
     */
    @ExcelProperty(value = "生日年龄")
    private Date patientBirthday;

    /**
     * 注册时间
     */
    @ExcelProperty(value = "注册时间")
    private Date patientRegisterDate;

    /**
     * 账户余额
     */
    @ExcelProperty(value = "账户余额")
    private BigDecimal patientMoney;

    /**
     *
     */
    @ExcelProperty(value = "")
    private String patientTest;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
