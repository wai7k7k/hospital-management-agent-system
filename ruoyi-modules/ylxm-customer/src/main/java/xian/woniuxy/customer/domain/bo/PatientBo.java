package xian.woniuxy.customer.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.math.BigDecimal;
import java.util.Date;
import com.ruoyi.common.core.web.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户业务对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PatientBo extends BaseEntity {

    /**
     * 病人唯一id
     */
    private Long patientId;

    /**
     * 手机号/账号
     */
    @NotBlank(message = "手机号/账号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String patientPhone;

    /**
     * 密码
     */
    private String patientPassword;

    /**
     * 名字
     */
    @NotBlank(message = "名字不能为空", groups = { AddGroup.class, EditGroup.class })
    private String patientName;

    /**
     * 性别
     */
    @NotBlank(message = "性别不能为空", groups = { AddGroup.class, EditGroup.class })
    private String patientSex;

    /**
     * 身份证
     */
    @NotBlank(message = "身份证不能为空", groups = { AddGroup.class, EditGroup.class })
    private String patientCardId;

    /**
     * 头像
     */
    private String patientPhoto;

    /**
     * 生日年龄
     */
    @NotNull(message = "生日年龄不能为空", groups = { AddGroup.class, EditGroup.class })
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date patientBirthday;

    /**
     * 注册时间
     */
    private Date patientRegisterDate;

    /**
     * 账户余额
     */
    private BigDecimal patientMoney;

    /**
     *
     */
    private String patientTest;

    /**
     * 备注
     */
    private String remark;


}
