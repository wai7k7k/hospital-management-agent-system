package xian.woniuxy.domain;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import com.ruoyi.common.core.web.domain.BaseEntity;
/**
 * @author G
 * @description: TODO
 * @date 2022/8/23 15:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inspect")
public class Inspect extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 检查项目id
     */
    @TableId(value = "inspect_id")
    private Long inspectId;
    /**
     * 检查项目名
     */
    private String inspectName;
    /**
     * 检查项目表名字
     */
    private String inspectTableName;
    /**
     * 报告类型: NUMERIC数值指标 / IMAGE_TEXT影像文本
     */
    private String itemType;
    /**
     * 检查项目费用
     */
    private BigDecimal inspectPrice;
    /**
     * 软删除 0删除/1默认
     */
    private Long softDel;
    /**
     * 备注
     */
    private String remark;

}
