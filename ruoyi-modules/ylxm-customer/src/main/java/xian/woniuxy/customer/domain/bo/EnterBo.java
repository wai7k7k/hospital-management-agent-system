package xian.woniuxy.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.util.Date;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 住院;业务对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class EnterBo extends BaseEntity {

    /**
     * 住院id
     */
    @NotNull(message = "住院id不能为空", groups = { EditGroup.class })
    private Long enterId;

    /**
     * 病人id
     */
    @NotNull(message = "病人id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long patientId;

    /**
     * 病人姓名
     */
    @NotBlank(message = "病人姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String patientName;

    /**
     * 病历id
     */
    @NotNull(message = "病历id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long recordId;

    /**
     * 主治医师id
     */
    @NotNull(message = "主治医师id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 主治医生名字
     */
    @NotBlank(message = "主治医生名字不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userName;

    /**
     * 科室id
     */
    @NotBlank(message = "科室id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String depId;

    /**
     * 科室名称
     */
    @NotBlank(message = "科室名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String depName;

    /**
     * 床位id
     */
    @NotNull(message = "床位id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long bedId;

    /**
     * 入院日期
     */
    @NotNull(message = "入院日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date enterDate;

    /**
     * 出院申请：0无/1申请/2已出
     */
    @NotNull(message = "出院申请：0无/1申请/2已出不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long outApply;

    /**
     * 出院日期
     */
    @NotNull(message = "出院日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date outData;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

    /**
     * 挂号id
     */
    @NotNull(message = "挂号id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long registerId;


}
