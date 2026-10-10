package xian.woniuxy.reghos.domain.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 手术项目业务对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class OperationBo extends BaseEntity {

    @TableField(exist = false)
    private String depName;

    @TableField(exist = false)
    private String[]  depNameList;
    /**
     * 手术项目id
     */
    @NotNull(message = "手术项目id不能为空", groups = { EditGroup.class })
    private Long operationId;

    /**
     * 手术名称
     */
    @NotBlank(message = "手术项目不能为空", groups = { AddGroup.class, EditGroup.class })
    private String operationName;

    /**
     * 科室id
     */
    private Long depId;

    /**
     * 手术费用
     */
    @NotNull(message = "手术价格不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal operationPrice;

    /**
     * 软删除 0删除/1默认
     */
    private Long softDel;

    /**
     * 备注
     */
    @NotBlank(message = "备注注意事项不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;




}
