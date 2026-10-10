package xian.woniuxy.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import java.math.BigDecimal;
import com.ruoyi.common.core.web.domain.BaseEntity;
/**
 * @author G
 * @description: TODO
 * @date 2022/8/23 15:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inspect_order")
public class InspectOrder extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 检查项目订单id
     */
    @TableId(value = "inspect_order_id")
    private Long inspectOrderId;
    /**
     * 病人id
     */
    private Long patientId;
    /**
     * 开单医师id
     */
    private Long userId;
    /**
     * 挂号id
     */
    private Long registerId;
    /**
     * 住院id
     */
    private Long enterId;
    /**
     * 检查项目id
     */
    private Long inspectId;
    /**
     * 检查项目时间
     */
    private Date inspectOrderDate;
    /**
     * 检查项目价格
     */
    private BigDecimal inspectOrderPrice;
    /**
     * 病人姓名
     */
    private String patientName;
    /**
     * 状态: 待采样 / 检测中 / 已出结果
     */
    private String status;
    /**
     * 备注
     */
    private String remark;

}

