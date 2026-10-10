package xian.woniuxy.reghos.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("exam_result")
public class ExamResult {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long applyId;
    private Long patientId;
    private Long registerId;
    private String itemType;
    private Date reportTime;
    private Long doctorId;
    private String summaryConclusion;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String remark;

    @TableField(exist = false)
    private java.util.List<ExamResultIndex> indices;
    @TableField(exist = false)
    private ExamResultImageText imageText;
}
