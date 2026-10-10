package xian.woniuxy.customer.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.core.validate.QueryGroup;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.excel.utils.ExcelUtil;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xian.woniuxy.customer.domain.bo.PatientBo;
import xian.woniuxy.customer.domain.vo.PatientVo;
import xian.woniuxy.customer.service.IPatientService;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户控制器
 * 前端访问路由地址为:/customer-serv/patient
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/patient")
public class PatientController extends BaseController {

    private final IPatientService iPatientService;

    /**
     * 查询用户列表
     */
//    @SaCheckPermission("woniuxy:patient:list")
    @GetMapping("/list")
    public TableDataInfo<PatientVo> list(PatientBo bo, PageQuery pageQuery) {
        return iPatientService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户列表
     */
//    @SaCheckPermission("woniuxy:patient:export")
    @Log(title = "用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PatientBo bo, HttpServletResponse response) {
        List<PatientVo> list = iPatientService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户", PatientVo.class, response);
    }

    /**
     * 获取用户详细信息
     *
     * @param patientId 主键
     */
//    @SaCheckPermission("woniuxy:patient:query")
    @GetMapping("/{patientId}")
    public R<PatientVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long patientId) {
        return R.ok(iPatientService.queryById(patientId));
    }

    /**
     * 新增用户
     */
//    @SaCheckPermission("woniuxy:patient:add")
    @Log(title = "用户", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PatientBo bo) {
        return toAjax(iPatientService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改用户
     */
//    @SaCheckPermission("woniuxy:patient:edit")
    @Log(title = "用户", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PatientBo bo) {
        return toAjax(iPatientService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除用户
     *
     * @param patientIds 主键串
     */
//    @SaCheckPermission("woniuxy:patient:remove")
    @Log(title = "用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{patientIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] patientIds) {
        return toAjax(iPatientService.deleteWithValidByIds(Arrays.asList(patientIds), true) ? 1 : 0);
    }
}
