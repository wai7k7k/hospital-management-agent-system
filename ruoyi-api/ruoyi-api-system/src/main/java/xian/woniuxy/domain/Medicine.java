package xian.woniuxy.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 药品对象 medicine
 *
 * @author ataoxz
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("medicine")
public class Medicine extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 药品id
     */
    @TableId(value = "medicine_id")
    private Long medicineId;
    /**
     * 药品名
     */
    private String medicineName;
    /**
     * 药品价格
     */
    private BigDecimal medicinePrice;
    /**
     * 药品库存
     */
    private Long medicineStock;
    /**
     * 药品注意事项
     */
    private String medicineDetails;
    /**
     * 软删除 0删除/1默认
     */
    private Long softDel;
    /**
     * 1处方/0非处方
     */
    private Long recipel;

}
