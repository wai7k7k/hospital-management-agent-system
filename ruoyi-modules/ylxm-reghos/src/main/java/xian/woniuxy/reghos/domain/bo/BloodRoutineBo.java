package xian.woniuxy.reghos.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 血常规结果;业务对象
 *
 * @author G
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BloodRoutineBo extends BaseEntity {

    /**
     * 血常规唯一id
     */
    @NotNull(message = "血常规唯一id不能为空", groups = { EditGroup.class })
    private Long brId;

    /**
     * 病人id
     */
//    @NotNull(message = "病人id不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * 白细胞
     */
    @NotBlank(message = "白细胞不能为空", groups = { AddGroup.class, EditGroup.class })
    private String Wbc;

    /**
     * 白细胞参考范围
     */
    @NotBlank(message = "白细胞参考范围不能为空", groups = { AddGroup.class, EditGroup.class })
    private String wbcReference;

    /**
     * 红细胞
     */
    @NotBlank(message = "红细胞不能为空", groups = { AddGroup.class, EditGroup.class })
    private String Rbc;



    /**
     * 红细胞参考范围
     */
    @NotBlank(message = "红细胞参考范围不能为空", groups = { AddGroup.class, EditGroup.class })
    private String rbcReference;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
