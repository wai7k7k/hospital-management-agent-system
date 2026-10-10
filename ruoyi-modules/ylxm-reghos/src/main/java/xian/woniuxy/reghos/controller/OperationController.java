package xian.woniuxy.reghos.controller;

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
import xian.woniuxy.reghos.domain.vo.OperationVo;
import xian.woniuxy.reghos.domain.bo.OperationBo;
import xian.woniuxy.reghos.service.IOperationService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 手术项目控制器
 * 前端访问路由地址为:/reghos-serv/operation
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/operation")
public class OperationController extends BaseController {

    private final IOperationService iOperationService;

    /**
     * 查询手术项目列表
     */
//    @SaCheckPermission("reghos-serv:operation:list")
    @GetMapping("/list")
    public TableDataInfo<OperationVo> list(OperationBo bo, PageQuery pageQuery) {
        return iOperationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出手术项目列表
     */
//    @SaCheckPermission("reghos-serv:operation:export")
    @Log(title = "手术项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(OperationBo bo, HttpServletResponse response) {
        List<OperationVo> list = iOperationService.queryList(bo);
        ExcelUtil.exportExcel(list, "手术项目", OperationVo.class, response);
    }

    /**
     * 获取手术项目详细信息
     *
     * @param operationId 主键
     */
//    @SaCheckPermission("reghos-serv:operation:query")
    @GetMapping("/{operationId}")
    public R<OperationVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long operationId) {
        return R.ok(iOperationService.queryById(operationId));
    }

    /**
     * 新增手术项目
     */
//    @SaCheckPermission("reghos-serv:operation:add")
    @Log(title = "手术项目", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody OperationBo bo) {
        return toAjax(iOperationService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改手术项目
     */
//    @SaCheckPermission("reghos-serv:operation:edit")
    @Log(title = "手术项目", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody OperationBo bo) {
        return toAjax(iOperationService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除手术项目
     *
     * @param operationIds 主键串
     */
//    @SaCheckPermission("reghos-serv:operation:remove")
    @Log(title = "手术项目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{operationIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] operationIds) {
        return toAjax(iOperationService.deleteWithValidByIds(Arrays.asList(operationIds), true) ? 1 : 0);
    }
}
