package xian.woniuxy.reghos.domain.vo;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 设备视图对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class EquipmentVo {

    private static final long serialVersionUID = 1L;

    /**
     * 设备id
     */
    @ExcelProperty(value = "设备id")
    private Long equipmentId;

    /**
     * 设备编号
     */
    @ExcelProperty(value = "设备编号")
    private String equipmentNum;

    /**
     * 设备名字
     */
    @ExcelProperty(value = "设备名字")
    private String equipmentName;

    /**
     * 设备添加时间
     */
    @ExcelProperty(value = "设备添加时间")
    private Date equipmentTime;

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
