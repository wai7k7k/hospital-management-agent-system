package com.ruoyi.system.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;
import java.math.BigDecimal;

import java.util.Date;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 排班对象 schedule
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("schedule")
public class Schedule extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 剩余名额
     */
    @TableField(exist = false)
    private Long shengyu;

    /**
     * 排班id
     */
    @TableId(value = "schedule_id")
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
