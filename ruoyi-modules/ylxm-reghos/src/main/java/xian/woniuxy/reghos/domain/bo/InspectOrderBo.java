package xian.woniuxy.reghos.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.math.BigDecimal;
import java.util.Date;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 检查项目订单;业务对象
 *
 * @author G
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class InspectOrderBo extends BaseEntity {

    /**
     * 检查项目订单id
     */
    @NotNull(message = "检查项目订单id不能为空", groups = { EditGroup.class })
    private Long inspectOrderId;

    /**
     * 病人id
     */
    @NotNull(message = "病人id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long patientId;

    /**
     * 开单医师id
     */
    @NotNull(message = "开单医师id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 挂号id
     */
    @NotNull(message = "挂号id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long registerId;

    /**
     * 住院id（门诊可为空）
     */
    private Long enterId;

    /**
     * 检查项目id
     */
    @NotNull(message = "检查项目id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long inspectId;

    /**
     * 检查项目时间
     */
    @NotNull(message = "检查项目时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date inspectOrderDate;

    /**
     * 检查项目价格
     */
    @NotNull(message = "检查项目价格不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal inspectOrderPrice;

    /**
     * 病人姓名
     */
    @NotBlank(message = "病人姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String patientName;

    /**
     * 备注（选填）
     */
    private String remark;


}
