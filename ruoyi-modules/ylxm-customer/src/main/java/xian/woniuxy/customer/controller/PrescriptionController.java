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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import xian.woniuxy.customer.domain.bo.PrescriptionBo;
import xian.woniuxy.customer.domain.vo.PrescriptionVo;
import xian.woniuxy.customer.service.IPrescriptionService;
import xian.woniuxy.customer.domain.bo.PrescriptionBo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 处方控制器
 * 前端访问路由地址为:/customer-serv/prescription
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/prescription")
public class PrescriptionController extends BaseController {

    private final IPrescriptionService iPrescriptionService;

    /**
     * 查询处方列表
     */
//    @SaCheckPermission("woniuxy:prescription:list")
    @GetMapping("/list")
    public TableDataInfo<PrescriptionVo> list(PrescriptionBo bo, PageQuery pageQuery) {
        return iPrescriptionService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出处方列表
     */
//    @SaCheckPermission("woniuxy:prescription:export")
    @Log(title = "处方", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PrescriptionBo bo, HttpServletResponse response) {
        List<PrescriptionVo> list = iPrescriptionService.queryList(bo);
        ExcelUtil.exportExcel(list, "处方", PrescriptionVo.class, response);
    }

    /**
     * 获取处方详细信息
     *
     * @param prescriptionId 主键
     */
//    @SaCheckPermission("woniuxy:prescription:query")
    @GetMapping("/{prescriptionId}")
    public R<PrescriptionVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long prescriptionId) {
        return R.ok(iPrescriptionService.queryById(prescriptionId));
    }

    /**
     * 新增处方
     */
//    @SaCheckPermission("woniuxy:prescription:add")
    @Log(title = "处方", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PrescriptionBo bo) {
        return toAjax(iPrescriptionService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改处方
     */
//    @SaCheckPermission("woniuxy:prescription:edit")
    @Log(title = "处方", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PrescriptionBo bo) {
        return toAjax(iPrescriptionService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除处方
     *
     * @param prescriptionIds 主键串
     */
//    @SaCheckPermission("woniuxy:prescription:remove")
    @Log(title = "处方", businessType = BusinessType.DELETE)
    @DeleteMapping("/{prescriptionIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] prescriptionIds) {
        return toAjax(iPrescriptionService.deleteWithValidByIds(Arrays.asList(prescriptionIds), true) ? 1 : 0);
    }
}
