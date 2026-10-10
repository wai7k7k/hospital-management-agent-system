package xian.woniuxy.reghos.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 肝功能结果业务对象
 *
 * @author G
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class LftsBo extends BaseEntity {

    /**
     * 肝功能id
     */
    @NotNull(message = "肝功能id不能为空", groups = { EditGroup.class })
    private Long lftsId;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * 谷丙转氨
     */
    @NotNull(message = "谷丙转氨不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long ALT;

    /**
     * 谷丙转氨参考值
     */
    @NotBlank(message = "谷丙转氨参考值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String ALTReference;

    /**
     * 总蛋白
     */
    @NotNull(message = "总蛋白不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long TP;

    /**
     * 总蛋白参考值
     */
    @NotBlank(message = "总蛋白参考值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String TPReference;

    /**
     * 白蛋白与球蛋白比值
     */
    @NotNull(message = "白蛋白与球蛋白比值不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long AG;

    /**
     * 白蛋白与球蛋白比值参考值
     */
    @NotBlank(message = "白蛋白与球蛋白比值参考值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String AGReference;

    /**
     * 总胆红素
     */
    @NotNull(message = "总胆红素不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long TBIL;

    /**
     * 总胆红素参考值
     */
    @NotBlank(message = "总胆红素参考值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String TBILReference;

    /**
     * 直接胆红素
     */
    @NotNull(message = "直接胆红素不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long DBIL;

    /**
     * 直接胆红素参考值
     */
    @NotBlank(message = "直接胆红素参考值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String DBILReference;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
