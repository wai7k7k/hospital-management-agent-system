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
 * 手术订单;业务对象
 *
 * @author G
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class OperationOrderBo extends BaseEntity {

    /**
     * 手术订单id
     */
    @NotNull(message = "手术订单id不能为空", groups = { EditGroup.class })
    private Long ooId;

    /**
     * 手术项目id
     */
    @NotNull(message = "手术项目id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long operationId;

    /**
     * 主刀医生
     */
    @NotNull(message = "主刀医生不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 主刀医生姓名
     */
    @NotBlank(message = "主刀医生姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userName;

    /**
     * 手术价格
     */
    @NotNull(message = "手术价格不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal operationPrice;

    /**
     * 手术订单时间
     */
    @NotNull(message = "手术订单时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date ooDate;

    /**
     * 手术时间
     */
    @NotNull(message = "手术时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date operationDate;

    /**
     * 家属是否已签字：是/否
     */
    @NotBlank(message = "家属是否已签字：是/否不能为空", groups = { AddGroup.class, EditGroup.class })
    private String opertationConfirm;

    /**
     * 挂号id
     */
    @NotNull(message = "挂号id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long registerId;

    /**
     * 住院id
     */
    @NotNull(message = "住院id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long enterId;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
