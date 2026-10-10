package xian.woniuxy.reghos.domain.vo;

import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 药品视图对象
 *
 * @author ataoxz
 * @date 2022-08-22
 */
@Data
@ExcelIgnoreUnannotated
public class MedicineVo {

    private static final long serialVersionUID = 1L;

    /**
     * 药品id
     */
    @ExcelProperty(value = "药品id")
    private Long medicineId;

    /**
     * 药品名
     */
    @ExcelProperty(value = "药品名")
    private String medicineName;

    /**
     * 药品价格
     */
    @ExcelProperty(value = "药品价格")
    private BigDecimal medicinePrice;

    /**
     * 药品库存
     */
    @ExcelProperty(value = "药品库存")
    private Long medicineStock;

    /**
     * 药品注意事项
     */
    @ExcelProperty(value = "药品注意事项")
    private String medicineDetails;

    /**
     * 软删除 0删除/1默认
     */
    @ExcelProperty(value = "软删除 0删除/1默认")
    private Long softDel;

    /**
     * 1处方/0非处方
     */
    @ExcelProperty(value = "1处方/0非处方", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "chufang_feichufang")
    private Long recipel;


}
