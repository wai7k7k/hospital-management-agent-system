package xian.woniuxy.reghos.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 科室;视图对象
 *
 * @author G
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class DepVo {

    private static final long serialVersionUID = 1L;

    /**
     * 科室id
     */
    @ExcelProperty(value = "科室id")
    private Long depId;

    /**
     * 科室名
     */
    @ExcelProperty(value = "科室名")
    private String depName;

    /**
     * 软删除 0删除/1默认
     */
    @ExcelProperty(value = "软删除 0删除/1默认")
    private Long softDel;


}
