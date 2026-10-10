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
 * @author G
 * @description: TODO
 * @date 2022/8/23 15:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dep")
public class Dep extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 科室id
     */
    @TableId(value = "dep_id")
    private Long depId;
    /**
     * 科室名
     */
    private String depName;
    /**
     * 软删除 0删除/1默认
     */
    private Long softDel;

}

