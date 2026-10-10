package xian.woniuxy.domain;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ruoyi.common.core.web.domain.BaseEntity;
/**
 * @author G
 * @description: TODO
 * @date 2022/8/23 15:32
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bed")
public class Bed extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 床位id
     */
    @TableId(value = "bed_id")
    private Long bedId;
    /**
     * 床位编号，唯一，命名：科室-编号
     */
    private String bedNum;
    /**
     * 科室id
     */
    private Long depId;
    /**
     * 床位状态，0无人，1有人
     */
    private Long bedState;
    /**
     * 软删除 0删除/1默认
     */
    private Long softDel;
    /**
     * 备注
     */
    private String remark;

}

