package xian.woniuxy.reghos.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.util.Date;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 设备业务对象
 *
 * @author ataoxz
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class EquipmentBo extends BaseEntity {

    /**
     * 设备id
     */
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
