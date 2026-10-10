package xian.woniuxy.reghos.service;

import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import xian.woniuxy.reghos.domain.bo.ExamResultBo;
import xian.woniuxy.reghos.domain.vo.ExamResultVo;
import java.util.List;

public interface IExamResultService {
    ExamResultVo queryById(Long id);
    TableDataInfo<ExamResultVo> queryPageList(ExamResultBo bo, PageQuery pageQuery);
    List<ExamResultVo> queryByPatientId(Long patientId);
    List<ExamResultVo> queryByRegisterId(Long registerId);
    Boolean insertByBo(ExamResultBo bo);
    Boolean updateByBo(ExamResultBo bo);
    Boolean deleteByIds(List<Long> ids);
    Boolean updateStatus(Long applyId, String status);
}
