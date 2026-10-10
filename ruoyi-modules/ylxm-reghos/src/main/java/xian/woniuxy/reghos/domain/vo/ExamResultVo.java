package xian.woniuxy.reghos.domain.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class ExamResultVo {
    private Long id;
    private Long applyId;
    private Long patientId;
    private String patientName;
    private Long registerId;
    private String itemType;
    private Date reportTime;
    private Long doctorId;
    private String doctorName;
    private String summaryConclusion;
    private String applyStatus;
    private String inspectName;
    private Date createTime;
    private String remark;
    private List<ExamResultIndexVo> indices;
    private ExamResultImageTextVo imageText;

    @Data
    public static class ExamResultIndexVo {
        private Long id;
        private String indexName;
        private String value;
        private String reference;
        private Integer isAbnormal;
    }

    @Data
    public static class ExamResultImageTextVo {
        private Long id;
        private String checkContent;
        private String checkConclusion;
    }
}
