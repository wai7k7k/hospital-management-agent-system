package xian.woniuxy.reghos.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("exam_result_image_text")
public class ExamResultImageText {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long resultId;
    private String checkContent;
    private String checkConclusion;
}
