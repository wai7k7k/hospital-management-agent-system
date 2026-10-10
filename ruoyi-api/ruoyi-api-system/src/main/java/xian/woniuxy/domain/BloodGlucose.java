package xian.woniuxy.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ruoyi.common.core.web.domain.BaseEntity;
/**
 * @author G
 * @description: TODO
 * @date 2022/8/23 15:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blood_glucose")
public class BloodGlucose extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 血糖唯一id
     */
    @TableId(value = "bg_id")
    private Long bgId;
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
     * 葡萄糖
     */
    private String GLU;
    /**
     * 葡萄糖参考范围
     */
    private String gluReference;
    /**
     * 酸碱度
     */
    private String PH;
    /**
     * 酸碱度参考范围
     */
    private String phReference;
    /**
     * 备注
     */
    private String remark;

}
