package xian.woniuxy.customer.domain.vo;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 处方视图对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class PrescriptionVo {

    private static final long serialVersionUID = 1L;

    /**
     * 处方id;
     */
    @ExcelProperty(value = "处方id;")
    private Long prescriptionId;

    /**
     * 病人id;
     */
    @ExcelProperty(value = "病人id;")
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
     * 病历id;
     */
    @ExcelProperty(value = "病历id;")
    private Long recordId;

    /**
     * 科室id;
     */
    @ExcelProperty(value = "科室id;")
    private Long depId;

    /**
     * 科室名称
     */
    @ExcelProperty(value = "科室名称")
    private String depName;

    /**
     * 日期;
     */
    @ExcelProperty(value = "日期;")
    private Date prescriptionDate;

    /**
     * 医生id;
     */
    @ExcelProperty(value = "医生id;")
    private Long userId;

    /**
     * 医生姓名
     */
    @ExcelProperty(value = "医生姓名")
    private String userName;

    /**
     * 药品及用量;
     */
    @ExcelProperty(value = "药品及用量;")
    private String rpMedicine;

    /**
     * 检查项目
     */
    @ExcelProperty(value = "检查项目")
    private String rpInspect;

    /**
     * 临床诊断;
     */
    @ExcelProperty(value = "临床诊断;")
    private String diagnosis;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
