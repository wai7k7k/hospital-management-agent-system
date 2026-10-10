package xian.woniuxy.customer.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.util.Date;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 处方业务对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PrescriptionBo extends BaseEntity {

    /**
     * 处方id;
     */
    private Long prescriptionId;

    /**
     * 病人id;
     */
    @NotNull(message = "病人id;不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long patientId;

    /**
     * 病人姓名
     */
    @NotBlank(message = "病人姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String patientName;

    /**
     * 挂号id
     */
    @NotNull(message = "挂号id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long registerId;

    /**
     * 病历id;
     */
    private Long recordId;

    /**
     * 科室id;
     */
    @NotNull(message = "科室id;不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long depId;

    /**
     * 科室名称
     */
    @NotBlank(message = "科室名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String depName;

    /**
     * 日期;
     */
    private Date prescriptionDate;

    /**
     * 医生id;
     */
    @NotNull(message = "医生id;不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 医生姓名
     */
    @NotBlank(message = "医生姓名不能为空", groups = { AddGroup.class, EditGroup.class })
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
