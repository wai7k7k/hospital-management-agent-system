package xian.woniuxy.reghos.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 病床床位;视图对象
 *
 * @author G
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class BedVo {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String depName;

    /**
     * 床位id
     */
    @ExcelProperty(value = "床位id")
    private Long bedId;

    /**
     * 床位编号，唯一，命名：科室-编号
     */
    @ExcelProperty(value = "床位编号，唯一，命名：科室-编号")
    private String bedNum;

    /**
     * 科室id
     */
    @ExcelProperty(value = "科室id")
    private Long depId;

    /**
     * 床位状态，0无人，1有人
     */
    @ExcelProperty(value = "床位状态，0无人，1有人")
    private Long bedState;

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
