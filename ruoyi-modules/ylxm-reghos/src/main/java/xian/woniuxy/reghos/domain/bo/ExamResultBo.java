package xian.woniuxy.reghos.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ruoyi.common.core.web.domain.BaseEntity;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExamResultBo extends BaseEntity {
    private Long id;
    private Long applyId;
    private Long patientId;
    private Long registerId;
    private String itemType;
    private String reportTime;
    private Long doctorId;
    private String summaryConclusion;
    private List<ExamResultIndexBo> indices;
    private ExamResultImageTextBo imageText;

    @Data
    public static class ExamResultIndexBo {
        private String indexName;
        private String value;
        private String reference;
        private Integer isAbnormal;
    }

    @Data
    public static class ExamResultImageTextBo {
        private String checkContent;
        private String checkConclusion;
    }
}
