package xian.woniuxy.reghos.domain.vo;

import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 手术项目视图对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class OperationVo {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String depName;
    /**
     * 手术项目id
     */
    @ExcelProperty(value = "手术项目id")
    private Long operationId;

    /**
     * 手术名称
     */
    @ExcelProperty(value = "手术名称")
    private String operationName;

    /**
     * 科室id
     */
    @ExcelProperty(value = "科室id")
    private Long depId;

    /**
     * 手术费用
     */
    @ExcelProperty(value = "手术费用")
    private BigDecimal operationPrice;

    /**
     * 软删除 0删除/1默认
     */
    @ExcelProperty(value = "软删除 0删除/1默认")
    private Long softDel;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

}
