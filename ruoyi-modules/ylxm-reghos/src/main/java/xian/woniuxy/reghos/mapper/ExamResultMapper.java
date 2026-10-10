package xian.woniuxy.reghos.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import xian.woniuxy.reghos.domain.ExamResult;
import xian.woniuxy.reghos.domain.vo.ExamResultVo;
import org.apache.ibatis.annotations.*;

public interface ExamResultMapper extends BaseMapperPlus<ExamResultMapper, ExamResult, ExamResultVo> {

    @Insert("INSERT INTO exam_result_index (result_id, index_name, value, reference, is_abnormal) " +
            "VALUES (#{resultId}, #{indexName}, #{value}, #{reference}, #{isAbnormal})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertIndex(xian.woniuxy.reghos.domain.ExamResultIndex idx);

    @Insert("INSERT INTO exam_result_image_text (result_id, check_content, check_conclusion) " +
            "VALUES (#{resultId}, #{checkContent}, #{checkConclusion})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertImageText(xian.woniuxy.reghos.domain.ExamResultImageText it);

    @Delete("DELETE FROM exam_result_index WHERE result_id = #{resultId}")
    int deleteIndicesByResultId(@Param("resultId") Long resultId);

    @Delete("DELETE FROM exam_result_image_text WHERE result_id = #{resultId}")
    int deleteImageTextByResultId(@Param("resultId") Long resultId);

    @Select("SELECT * FROM exam_result_index WHERE result_id = #{resultId}")
    java.util.List<xian.woniuxy.reghos.domain.ExamResultIndex> selectIndicesByResultId(@Param("resultId") Long resultId);

    @Select("SELECT * FROM exam_result_image_text WHERE result_id = #{resultId} LIMIT 1")
    xian.woniuxy.reghos.domain.ExamResultImageText selectImageTextByResultId(@Param("resultId") Long resultId);
}
