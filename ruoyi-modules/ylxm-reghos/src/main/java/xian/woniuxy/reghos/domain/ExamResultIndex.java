package xian.woniuxy.reghos.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("exam_result_index")
public class ExamResultIndex {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long resultId;
    private String indexName;
    private String value;
    private String reference;
    private Integer isAbnormal;
}
