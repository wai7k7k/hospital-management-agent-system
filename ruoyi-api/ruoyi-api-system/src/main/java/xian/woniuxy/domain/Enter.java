package xian.woniuxy.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.util.Date;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 住院;对象 enter
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("enter")
public class Enter extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 住院id
     */
    @TableId(value = "enter_id")
    private Long enterId;
    /**
     * 病人id
     */
    private Long patientId;
    /**
     * 病人姓名
     */
    private String patientName;
    /**
     * 病历id
     */
    private Long recordId;
    /**
     * 主治医师id
     */
    private Long userId;
    /**
     * 主治医生名字
     */
    private String userName;
    /**
     * 科室id
     */
    private Long depId;
    /**
     * 科室名称
     */
    private String depName;
    /**
     * 床位id
     */
    private Long bedId;
    /**
     * 入院日期
     */
    private Date enterDate;
    /**
     * 出院申请：0无/1申请/2已出
     */
    private Long outApply;
    /**
     * 出院日期
     */
    private Date outData;
    /**
     * 备注
     */
    private String remark;
    /**
     * 挂号id
     */
    private Long registerId;

}
