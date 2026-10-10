package com.ruoyi.system.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.time.LocalDate;
import com.ruoyi.common.core.web.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 排班业务对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleBo extends BaseEntity {

    /**
     * 排班id
     */
    private Long scheduleId;

    /**
     * 医生id
     */
    private Long userId;

    /**
     * 医生姓名
     */
    private String userName;

    /**
     * 科室id
     */
    private Long depId;

    /**
     * 科室名称
     */
    private String depName;

    /**
     * 医生头像
     */
    private String avatar;

    /**
     * 排班日期
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate scheduleDate;

    /**
     * 上午，下午
     */
    private String scheduleTime;

    /**
     * 接诊人数
     */
    private Long scheduleNum;

    /**
     * 备注
     */
    private String remark;


}
