package com.ruoyi.system.domain.vo;

import java.time.LocalDate;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.excel.annotation.ExcelDictFormat;
import com.ruoyi.common.excel.convert.ExcelDictConvert;
import lombok.Data;



/**
 * 排班视图对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@ExcelIgnoreUnannotated
public class ScheduleVo {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private Long shengyu;

    /**
     * 排班id
     */
    @ExcelProperty(value = "排班id")
    private Long scheduleId;

    /**
     * 医生id
     */
    @ExcelProperty(value = "医生id")
    private Long userId;

    /**
     * 医生姓名
     */
    @ExcelProperty(value = "医生姓名")
    private String userName;

    /**
     * 科室id
     */
    @ExcelProperty(value = "科室id")
    private Long depId;

    /**
     * 科室名称
     */
    @ExcelProperty(value = "科室名称")
    private String depName;

    /**
     * 医生头像
     */
    @ExcelProperty(value = "医生头像")
    private String avatar;

    /**
     * 排班日期
     */
    @ExcelProperty(value = "排班日期")
    private LocalDate scheduleDate;

    /**
     * 上午，下午
     */
    @ExcelProperty(value = "上午，下午")
    private String scheduleTime;

    /**
     * 接诊人数
     */
    @ExcelProperty(value = "接诊人数")
    private Long scheduleNum;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
