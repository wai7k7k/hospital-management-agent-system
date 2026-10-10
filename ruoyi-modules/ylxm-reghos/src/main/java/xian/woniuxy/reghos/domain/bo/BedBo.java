package xian.woniuxy.reghos.domain.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 病床床位;业务对象
 *
 * @author G
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BedBo extends BaseEntity {

    @TableField(exist = false)
    private String depName;

    /**
     * 床位id
     */
//    @NotNull(message = "床位id不能为空", groups = { EditGroup.class })
    private Long bedId;

    /**
     * 床位编号，唯一，命名：科室-编号
     */
//    @NotBlank(message = "床位编号，唯一，命名：科室-编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String bedNum;

    /**
     * 科室id
     */
//    @NotNull(message = "科室id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long depId;

    /**
     * 床位状态，0无人，1有人
     */
//    @NotNull(message = "床位状态，0无人，1有人不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long bedState;

    /**
     * 软删除 0删除/1默认
     */
//    @NotNull(message = "软删除 0删除/1默认不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long softDel;

    /**
     * 备注
     */
//    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
