package xian.woniuxy.reghos.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.math.BigDecimal;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 检查项目;业务对象
 *
 * @author G
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class InspectBo extends BaseEntity {

    /**
     * 检查项目id
     */
    @NotNull(message = "检查项目id不能为空", groups = { EditGroup.class })
    private Long inspectId;

    /**
     * 检查项目名
     */
    @NotBlank(message = "检查项目名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String inspectName;

    /**
     * 检查项目表名字
     */
    @NotBlank(message = "检查项目表名字不能为空", groups = { AddGroup.class, EditGroup.class })
    private String inspectTableName;

    /**
     * 检查项目费用
     */
    @NotNull(message = "检查项目费用不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal inspectPrice;

    /**
     * 软删除 0删除/1默认
     */
    @NotNull(message = "软删除 0删除/1默认不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long softDel;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
