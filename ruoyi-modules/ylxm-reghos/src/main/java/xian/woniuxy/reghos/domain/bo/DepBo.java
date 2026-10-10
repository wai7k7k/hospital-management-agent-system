package xian.woniuxy.reghos.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 科室;业务对象
 *
 * @author G
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DepBo extends BaseEntity {

    /**
     * 科室id
     */
    @NotNull(message = "科室id不能为空", groups = { EditGroup.class })
    private Long depId;

    /**
     * 科室名
     */
    @NotBlank(message = "科室名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String depName;

    /**
     * 软删除 0删除/1默认
     */
    @NotNull(message = "软删除 0删除/1默认不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long softDel;


}
