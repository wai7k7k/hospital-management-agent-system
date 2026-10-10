package xian.woniuxy.reghos.domain.vo;

import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 检查项目;视图对象
 *
 * @author G
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class InspectVo {

    private static final long serialVersionUID = 1L;

    /**
     * 检查项目id
     */
    @ExcelProperty(value = "检查项目id")
    private Long inspectId;

    /**
     * 检查项目名
     */
    @ExcelProperty(value = "检查项目名")
    private String inspectName;

    /**
     * 检查项目表名字
     */
    @ExcelProperty(value = "检查项目表名字")
    private String inspectTableName;

    /**
     * 检查项目费用
     */
    @ExcelProperty(value = "检查项目费用")
    private BigDecimal inspectPrice;

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
