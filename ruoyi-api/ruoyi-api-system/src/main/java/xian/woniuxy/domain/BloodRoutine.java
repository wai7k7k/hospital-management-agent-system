package xian.woniuxy.domain;

/**
 * @author G
 * @description: TODO
 * @date 2022/8/23 15:34
 */

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.ruoyi.common.core.web.domain.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blood_routine")
public class BloodRoutine extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 血常规唯一id
     */
    @TableId(value = "br_id")
    private Long brId;
    /**
     * 病人id
     */
    private Long patientId;
    /**
     * 病人姓名
     */
    private String patientName;
    /**
     * 挂号id
     */
    private Long registerId;
    /**
     * 白细胞
     */
    private String WBC;
    /**
     * 白细胞参考范围
     */
    private String wbcReference;
    /**
     * 红细胞
     */
    private String RBC;
    /**
     * 红细胞参考范围
     */
    private String rbcReference;
    /**
     * 备注
     */
    private String remark;

}

