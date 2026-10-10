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
 * 挂号对象 register
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("register")
public class Register extends BaseEntity {

    private static final long serialVersionUID=1L;

    private String patientName;

    /**
     * 挂号id
     */
    @TableId(value = "register_id")
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
    private String remark;

}
