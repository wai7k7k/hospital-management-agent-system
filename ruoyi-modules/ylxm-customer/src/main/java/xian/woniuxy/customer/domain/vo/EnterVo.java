package xian.woniuxy.domain.vo;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 住院;视图对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class EnterVo {

    private static final long serialVersionUID = 1L;

    /**
     * 住院id
     */
    @ExcelProperty(value = "住院id")
    private Long enterId;

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
     * 病历id
     */
    @ExcelProperty(value = "病历id")
    private Long recordId;

    /**
     * 主治医师id
     */
    @ExcelProperty(value = "主治医师id")
    private Long userId;

    /**
     * 主治医生名字
     */
    @ExcelProperty(value = "主治医生名字")
    private String userName;

    /**
     * 科室id
     */
    @ExcelProperty(value = "科室id")
    private String depId;

    /**
     * 科室名称
     */
    @ExcelProperty(value = "科室名称")
    private String depName;

    /**
     * 床位id
     */
    @ExcelProperty(value = "床位id")
    private Long bedId;

    /**
     * 入院日期
     */
    @ExcelProperty(value = "入院日期")
    private Date enterDate;

    /**
     * 出院申请：0无/1申请/2已出
     */
    @ExcelProperty(value = "出院申请：0无/1申请/2已出")
    private Long outApply;

    /**
     * 出院日期
     */
    @ExcelProperty(value = "出院日期")
    private Date outData;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 挂号id
     */
    @ExcelProperty(value = "挂号id")
    private Long registerId;


}
