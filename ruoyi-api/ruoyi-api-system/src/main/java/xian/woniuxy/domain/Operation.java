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
 * 手术项目对象 operation
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("operation")
public class Operation extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 手术项目id
     */
    @TableId(value = "operation_id")
    private Long operationId;
    /**
     * 手术名称
     */
    private String operationName;
    /**
     * 科室id
     */
    private Long depId;
    /**
     * 手术费用
     */
    private BigDecimal operationPrice;
    /**
     * 软删除 0删除/1默认
     */
    private Long softDel;
    /**
     * 备注
     */
    private String remark;

}
