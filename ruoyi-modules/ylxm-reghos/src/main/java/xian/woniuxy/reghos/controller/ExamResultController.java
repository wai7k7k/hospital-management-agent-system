package xian.woniuxy.reghos.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xian.woniuxy.reghos.domain.bo.ExamResultBo;
import xian.woniuxy.reghos.domain.vo.ExamResultVo;
import xian.woniuxy.reghos.service.IExamResultService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/exam_result")
public class ExamResultController extends BaseController {

    private final IExamResultService examResultService;

    /** 分页查询（管理员） */
    @GetMapping("/list")
    public TableDataInfo<ExamResultVo> list(ExamResultBo bo, PageQuery pageQuery) {
        return examResultService.queryPageList(bo, pageQuery);
    }

    /** 详情 */
    @GetMapping("/{id}")
    public R<ExamResultVo> getInfo(@PathVariable Long id) {
        return R.ok(examResultService.queryById(id));
    }

    /** 新增（录入检查结果） */
    @PostMapping()
    public R<Void> add(@RequestBody ExamResultBo bo) {
        return toAjax(examResultService.insertByBo(bo) ? 1 : 0);
    }

    /** 修改 */
    @PutMapping()
    public R<Void> edit(@RequestBody ExamResultBo bo) {
        return toAjax(examResultService.updateByBo(bo) ? 1 : 0);
    }

    /** 删除 */
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(examResultService.deleteByIds(java.util.Arrays.asList(ids)) ? 1 : 0);
    }

    /** 患者端：查询本人所有结果 */
    @GetMapping("/patient/{patientId}")
    public R<List<ExamResultVo>> getByPatient(@PathVariable Long patientId) {
        return R.ok(examResultService.queryByPatientId(patientId));
    }

    /** 就诊页：按挂号ID查本次全部结果 */
    @GetMapping("/register/{registerId}")
    public R<List<ExamResultVo>> getByRegister(@PathVariable Long registerId) {
        return R.ok(examResultService.queryByRegisterId(registerId));
    }
}
