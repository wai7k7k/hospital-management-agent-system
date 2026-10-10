package xian.woniuxy.reghos.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 肝功能结果视图对象
 *
 * @author G
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class LftsVo {

    private static final long serialVersionUID = 1L;

    /**
     * 肝功能id
     */
    @ExcelProperty(value = "肝功能id")
    private Long lftsId;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
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
     * 谷丙转氨
     */
    @ExcelProperty(value = "谷丙转氨")
    private Long ALT;

    /**
     * 谷丙转氨参考值
     */
    @ExcelProperty(value = "谷丙转氨参考值")
    private String ALTReference;

    /**
     * 总蛋白
     */
    @ExcelProperty(value = "总蛋白")
    private Long TP;

    /**
     * 总蛋白参考值
     */
    @ExcelProperty(value = "总蛋白参考值")
    private String TPReference;

    /**
     * 白蛋白与球蛋白比值
     */
    @ExcelProperty(value = "白蛋白与球蛋白比值")
    private Long AG;

    /**
     * 白蛋白与球蛋白比值参考值
     */
    @ExcelProperty(value = "白蛋白与球蛋白比值参考值")
    private String AGReference;

    /**
     * 总胆红素
     */
    @ExcelProperty(value = "总胆红素")
    private Long TBIL;

    /**
     * 总胆红素参考值
     */
    @ExcelProperty(value = "总胆红素参考值")
    private String TBILReference;

    /**
     * 直接胆红素
     */
    @ExcelProperty(value = "直接胆红素")
    private Long DBIL;

    /**
     * 直接胆红素参考值
     */
    @ExcelProperty(value = "直接胆红素参考值")
    private String DBILReference;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
