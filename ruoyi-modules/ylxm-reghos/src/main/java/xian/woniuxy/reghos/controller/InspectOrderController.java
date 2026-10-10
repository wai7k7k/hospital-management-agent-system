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
import xian.woniuxy.reghos.domain.vo.InspectOrderVo;
import xian.woniuxy.reghos.domain.bo.InspectOrderBo;
import xian.woniuxy.reghos.service.IInspectOrderService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 检查项目订单;控制器
 * 前端访问路由地址为:/reghos-serv/inspect_orderr
 *
 * @author G
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/inspect_orderr")
public class InspectOrderController extends BaseController {

    private final IInspectOrderService iInspectOrderService;

    /**
     * 查询检查项目订单;列表
     */
//    @SaCheckPermission("reghos-serv:inspect_orderr:list")
    @GetMapping("/list")
    public TableDataInfo<InspectOrderVo> list(InspectOrderBo bo, PageQuery pageQuery) {
        return iInspectOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出检查项目订单;列表
     */
//    @SaCheckPermission("reghos-serv:inspect_orderr:export")
    @Log(title = "检查项目订单;", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(InspectOrderBo bo, HttpServletResponse response) {
        List<InspectOrderVo> list = iInspectOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "检查项目订单;", InspectOrderVo.class, response);
    }

    /**
     * 获取检查项目订单;详细信息
     *
     * @param inspectOrderId 主键
     */
//    @SaCheckPermission("reghos-serv:inspect_orderr:query")
    @GetMapping("/{inspectOrderId}")
    public R<InspectOrderVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long inspectOrderId) {
        return R.ok(iInspectOrderService.queryById(inspectOrderId));
    }

    /**
     * 新增检查项目订单;
     */
//    @SaCheckPermission("reghos-serv:inspect_orderr:add")
    @Log(title = "检查项目订单;", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody InspectOrderBo bo) {
        return toAjax(iInspectOrderService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改检查项目订单;
     */
//    @SaCheckPermission("reghos-serv:inspect_orderr:edit")
    @Log(title = "检查项目订单;", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody InspectOrderBo bo) {
        return toAjax(iInspectOrderService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除检查项目订单;
     *
     * @param inspectOrderIds 主键串
     */
//    @SaCheckPermission("reghos-serv:inspect_orderr:remove")
    @Log(title = "检查项目订单;", businessType = BusinessType.DELETE)
    @DeleteMapping("/{inspectOrderIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] inspectOrderIds) {
        return toAjax(iInspectOrderService.deleteWithValidByIds(Arrays.asList(inspectOrderIds), true) ? 1 : 0);
    }
}
