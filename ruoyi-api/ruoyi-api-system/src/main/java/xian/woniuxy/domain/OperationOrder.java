package xian.woniuxy.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.Date;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 手术订单;对象 operation_order
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("operation_order")
public class OperationOrder extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 手术订单id
     */
    @TableId(value = "oo_id")
    private Long ooId;
    /**
     * 手术项目id
     */
    private Long operationId;
    /**
     * 主刀医生
     */
    private Long userId;
    /**
     * 主刀医生姓名
     */
    private String userName;
    /**
     * 手术价格
     */
    private BigDecimal operationPrice;
    /**
     * 手术订单时间
     */
    private Date ooDate;
    /**
     * 手术时间
     */
    private Date operationDate;
    /**
     * 家属是否已签字：是/否
     */
    private String opertationConfirm;
    /**
     * 挂号id
     */
    private Long registerId;
    /**
     * 住院id
     */
    private Long enterId;
    /**
     * 备注
     */
    private String remark;

}
