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
import xian.woniuxy.reghos.domain.vo.InspectVo;
import xian.woniuxy.reghos.domain.bo.InspectBo;
import xian.woniuxy.reghos.service.IInspectService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 检查项目;控制器
 * 前端访问路由地址为:/reghos-serv/inspect
 *
 * @author G
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/inspect")
public class InspectController extends BaseController {

    private final IInspectService iInspectService;

    /**
     * 查询检查项目;列表
     */
//    @SaCheckPermission("reghos-serv:inspect:list")
    @GetMapping("/list")
    public TableDataInfo<InspectVo> list(InspectBo bo, PageQuery pageQuery) {
        return iInspectService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出检查项目;列表
     */
//    @SaCheckPermission("reghos-serv:inspect:export")
    @Log(title = "检查项目;", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(InspectBo bo, HttpServletResponse response) {
        List<InspectVo> list = iInspectService.queryList(bo);
        ExcelUtil.exportExcel(list, "检查项目;", InspectVo.class, response);
    }

    /**
     * 获取检查项目;详细信息
     *
     * @param inspectId 主键
     */
//    @SaCheckPermission("reghos-serv:inspect:query")
    @GetMapping("/{inspectId}")
    public R<InspectVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long inspectId) {
        return R.ok(iInspectService.queryById(inspectId));
    }

    /**
     * 新增检查项目;
     */
//    @SaCheckPermission("reghos-serv:inspect:add")
    @Log(title = "检查项目;", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody InspectBo bo) {
        return toAjax(iInspectService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改检查项目;
     */
//    @SaCheckPermission("reghos-serv:inspect:edit")
    @Log(title = "检查项目;", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody InspectBo bo) {
        return toAjax(iInspectService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除检查项目;
     *
     * @param inspectIds 主键串
     */
//    @SaCheckPermission("reghos-serv:inspect:remove")
    @Log(title = "检查项目;", businessType = BusinessType.DELETE)
    @DeleteMapping("/{inspectIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] inspectIds) {
        return toAjax(iInspectService.deleteWithValidByIds(Arrays.asList(inspectIds), true) ? 1 : 0);
    }
}
