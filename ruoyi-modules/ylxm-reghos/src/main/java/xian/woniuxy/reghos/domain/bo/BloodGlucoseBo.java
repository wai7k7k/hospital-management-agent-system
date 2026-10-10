package xian.woniuxy.reghos.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 血糖结果;业务对象
 *
 * @author G
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BloodGlucoseBo extends BaseEntity {

    /**
     * 血糖唯一id
     */
    @NotNull(message = "血糖唯一id不能为空", groups = { EditGroup.class })
    private Long bgId;

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
     * 葡萄糖
     */
//    @NotBlank(message = "葡萄糖不能为空", groups = { AddGroup.class, EditGroup.class })
    private String GLU;

    /**
     * 葡萄糖参考范围
     */
    //@NotBlank(message = "葡萄糖参考范围不能为空", groups = { AddGroup.class, EditGroup.class })
    private String gluReference;

    /**
     * 酸碱度
     */
//    @NotBlank(message = "酸碱度不能为空", groups = { AddGroup.class, EditGroup.class })
    private String PH;

    /**
     * 酸碱度参考范围
     */
    //@NotBlank(message = "酸碱度参考范围不能为空", groups = { AddGroup.class, EditGroup.class })
    private String phReference;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
