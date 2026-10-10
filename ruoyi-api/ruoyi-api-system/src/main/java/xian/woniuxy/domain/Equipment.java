package xian.woniuxy.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.util.Date;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 设备对象 equipment
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("equipment")
public class Equipment extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 设备id
     */
    @TableId(value = "equipment_id")
    private Long equipmentId;
    /**
     * 设备编号
     */
    private String equipmentNum;
    /**
     * 设备名字
     */
    private String equipmentName;
    /**
     * 设备添加时间
     */
    private Date equipmentTime;
    /**
     * 软删除 0删除/1默认
     */
    private Long softDel;
    /**
     * 备注
     */
    private String remark;

}
