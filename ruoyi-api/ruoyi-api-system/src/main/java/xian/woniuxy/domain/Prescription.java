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
 * 处方对象 prescription
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("prescription")
public class Prescription extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 处方id;
     */
    @TableId(value = "prescription_id")
    private Long prescriptionId;
    /**
     * 病人id;
     */
    private Long patientId;
    /**
     * 病人姓名
     */
    private String patientName;
    /**
     * 挂号id
     */
    private Long registerId;
    /**
     * 病历id;
     */
    private Long recordId;
    /**
     * 科室id;
     */
    private Long depId;
    /**
     * 科室名称
     */
    private String depName;
    /**
     * 日期;
     */
    private Date prescriptionDate;
    /**
     * 医生id;
     */
    private Long userId;
    /**
     * 医生姓名
     */
    private String userName;
    /**
     * 药品及用量;
     */
    private String rpMedicine;
    /**
     * 检查项目
     */
    private String rpInspect;
    /**
     * 临床诊断;
     */
    private String diagnosis;
    /**
     * 备注
     */
    private String remark;

}
