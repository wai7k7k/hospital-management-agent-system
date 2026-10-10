package xian.woniuxy.reghos.domain.bo;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 挂号业务对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterBo extends BaseEntity {
    @TableField(exist = false)
    private String patientName;
    /**
     * 挂号id
     */
    private Long registerId;

    /**
     * 用户id
     */
    private Long patientId;

    /**
     * 医生id
     */
    private Long userId;

    /**
     * 医生姓名
     */
    private String userName;

    /**
     * 挂号时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date registerDate;

    /**
     * 科室id
     */
    private Long depId;

    /**
     * 科室名称
     */
    private String depName;

    /**
     * 挂号价格
     */
    private BigDecimal registerPrice;

    /**
     * 是否完成0未完成，1已完成
     */
    private String registerState;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空")
    private String remark = "AI自助挂号";


}
