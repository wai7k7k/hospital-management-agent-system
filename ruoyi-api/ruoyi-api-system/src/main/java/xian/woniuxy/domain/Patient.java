package xian.woniuxy.domain;

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
 * 用户对象 patient
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("patient")
public class Patient extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 病人唯一id
     */
    @TableId(value = "patient_id")
    private Long patientId;
    /**
     * 手机号/账号
     */
    private String patientPhone;
    /**
     * 密码
     */
    private String patientPassword;
    /**
     * 名字
     */
    private String patientName;
    /**
     * 性别
     */
    private String patientSex;
    /**
     * 身份证
     */
    private String patientCardId;
    /**
     * 头像
     */
    private String patientPhoto;
    /**
     * 生日年龄
     */
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
