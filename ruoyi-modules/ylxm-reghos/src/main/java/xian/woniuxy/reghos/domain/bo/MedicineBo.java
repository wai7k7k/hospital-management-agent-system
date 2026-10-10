package xian.woniuxy.reghos.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.math.BigDecimal;
import com.ruoyi.common.core.web.domain.BaseEntity;
import org.springframework.data.annotation.Id;

/**
 * 药品业务对象
 *
 * @author ataoxz
 * @date 2022-08-22
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class MedicineBo extends BaseEntity {

    /**
     * 药品id
     */
    @NotNull(message = "药品id不能为空", groups = { EditGroup.class })
    private Long medicineId;

    /**
     * 药品名
     */
    @NotBlank(message = "药品名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String medicineName;

    /**
     * 药品价格
     */
    @NotNull(message = "药品价格不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal medicinePrice;

    /**
     * 药品库存
     */
    @NotNull(message = "药品库存不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long medicineStock;

    /**
     * 药品注意事项
     */
    @NotBlank(message = "药品注意事项不能为空", groups = { AddGroup.class, EditGroup.class })
    private String medicineDetails;

    /**
     * 软删除 0删除/1默认
     */
//    @NotNull(message = "软删除 0删除/1默认不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long softDel;

    /**
     * 1处方/0非处方
     */
    @NotNull(message = "1处方/0非处方不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long recipel;


}
