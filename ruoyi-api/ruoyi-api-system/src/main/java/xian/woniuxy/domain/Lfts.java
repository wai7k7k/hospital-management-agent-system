package xian.woniuxy.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 肝功能结果对象 lfts
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lfts")
public class Lfts extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 肝功能id
     */
    @TableId(value = "lfts_id")
    private Long lftsId;
    /**
     * 用户id
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
     * 谷丙转氨
     */
    private Long ALT;
    /**
     * 谷丙转氨参考值
     */
    private String ALTReference;
    /**
     * 总蛋白
     */
    private Long TP;
    /**
     * 总蛋白参考值
     */
    private String TPReference;
    /**
     * 白蛋白与球蛋白比值
     */
    private Long AG;
    /**
     * 白蛋白与球蛋白比值参考值
     */
    private String AGReference;
    /**
     * 总胆红素
     */
    private Long TBIL;
    /**
     * 总胆红素参考值
     */
    private String TBILReference;
    /**
     * 直接胆红素
     */
    private Long DBIL;
    /**
     * 直接胆红素参考值
     */
    private String DBILReference;
    /**
     * 备注
     */
    private String remark;

}
