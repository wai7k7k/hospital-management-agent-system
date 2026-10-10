package xian.woniuxy.reghos.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 血常规结果;视图对象
 *
 * @author G
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class BloodRoutineVo {

    private static final long serialVersionUID = 1L;

    /**
     * 血常规唯一id
     */
    @ExcelProperty(value = "血常规唯一id")
    private Long brId;

    /**
     * 病人id
     */
    @ExcelProperty(value = "病人id")
    private Long patientId;

    /**
     * 病人姓名
     */
    @ExcelProperty(value = "病人姓名")
    private String patientName;

    /**
     * 挂号id
     */
    @ExcelProperty(value = "挂号id")
    private Long registerId;

    /**
     * 白细胞
     */
    @ExcelProperty(value = "白细胞")
    private String WBC;

    /**
     * 白细胞参考范围
     */
    @ExcelProperty(value = "白细胞参考范围")
    private String wbcReference;

    /**
     * 红细胞
     */
    @ExcelProperty(value = "红细胞")
    private String RBC;

    /**
     * 红细胞参考范围
     */
    @ExcelProperty(value = "红细胞参考范围")
    private String rbcReference;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
