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
 * 病历;病历对象 record
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("record")
public class Record extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 病历id
     */
    @TableId(value = "record_id")
    private Long recordId;
    /**
     * 病人id
     */
    private Long patientId;
    /**
     * 病人姓名
     */
    private String patientName;
    /**
     * 病情描述
     */
    private String recordDesc;
    /**
     * 健康状况
     */
    private String recordHealth;
    /**
     * 感染病史;感染病史(有/无，有就填备注)
     */
    private String recordInfect;
    /**
     * 感染病史备注
     */
    private String recordInfectDetails;
    /**
     * 过敏史;过敏史(有/无，有就填备注)
     */
    private String recordAllergy;
    /**
     * 过敏史备注
     */
    private String recordAllergyDetails;
    /**
     * 遗传病史;遗传病史(有/无，有就填备注)
     */
    private String recordInherit;
    /**
     * 遗传病史备注
     */
    private String recordInheritDetails;
    /**
     * 记录日期;最近一次记录日期
     */
    private Date recordDate;
    /**
     * 备注
     */
    private String remark;

}
