package xian.woniuxy.customer.domain.vo;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 病历;病历视图对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class RecordVo {

    private static final long serialVersionUID = 1L;

    /**
     * 病历id
     */
    @ExcelProperty(value = "病历id")
    private Long recordId;

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
     * 病情描述
     */
    @ExcelProperty(value = "病情描述")
    private String recordDesc;

    /**
     * 健康状况
     */
    @ExcelProperty(value = "健康状况")
    private String recordHealth;

    /**
     * 感染病史;感染病史(有/无，有就填备注)
     */
    @ExcelProperty(value = "感染病史;感染病史(有/无，有就填备注)")
    private String recordInfect;

    /**
     * 感染病史备注
     */
    @ExcelProperty(value = "感染病史备注")
    private String recordInfectDetails;

    /**
     * 过敏史;过敏史(有/无，有就填备注)
     */
    @ExcelProperty(value = "过敏史;过敏史(有/无，有就填备注)")
    private String recordAllergy;

    /**
     * 过敏史备注
     */
    @ExcelProperty(value = "过敏史备注")
    private String recordAllergyDetails;

    /**
     * 遗传病史;遗传病史(有/无，有就填备注)
     */
    @ExcelProperty(value = "遗传病史;遗传病史(有/无，有就填备注)")
    private String recordInherit;

    /**
     * 遗传病史备注
     */
    @ExcelProperty(value = "遗传病史备注")
    private String recordInheritDetails;

    /**
     * 记录日期;最近一次记录日期
     */
    @ExcelProperty(value = "记录日期;最近一次记录日期")
    private Date recordDate;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
