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
import xian.woniuxy.reghos.domain.vo.OperationOrderVo;
import xian.woniuxy.reghos.domain.bo.OperationOrderBo;
import xian.woniuxy.reghos.service.IOperationOrderService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 手术订单;控制器
 * 前端访问路由地址为:/reghos-serv/operation_order
 *
 * @author G
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/operation_order")
public class OperationOrderController extends BaseController {

    private final IOperationOrderService iOperationOrderService;

    /**
     * 查询手术订单;列表
     */
//    @SaCheckPermission("reghos-serv:operation_order:list")
    @GetMapping("/list")
    public TableDataInfo<OperationOrderVo> list(OperationOrderBo bo, PageQuery pageQuery) {
        return iOperationOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出手术订单;列表
     */
//    @SaCheckPermission("reghos-serv:operation_order:export")
    @Log(title = "手术订单;", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(OperationOrderBo bo, HttpServletResponse response) {
        List<OperationOrderVo> list = iOperationOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "手术订单;", OperationOrderVo.class, response);
    }

    /**
     * 获取手术订单;详细信息
     *
     * @param ooId 主键
     */
//    @SaCheckPermission("reghos-serv:operation_order:query")
    @GetMapping("/{ooId}")
    public R<OperationOrderVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long ooId) {
        return R.ok(iOperationOrderService.queryById(ooId));
    }

    /**
     * 新增手术订单;
     */
//    @SaCheckPermission("reghos-serv:operation_order:add")
    @Log(title = "手术订单;", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody OperationOrderBo bo) {
        return toAjax(iOperationOrderService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改手术订单;
     */
//    @SaCheckPermission("reghos-serv:operation_order:edit")
    @Log(title = "手术订单;", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody OperationOrderBo bo) {
        return toAjax(iOperationOrderService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除手术订单;
     *
     * @param ooIds 主键串
     */
//    @SaCheckPermission("reghos-serv:operation_order:remove")
    @Log(title = "手术订单;", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ooIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ooIds) {
        return toAjax(iOperationOrderService.deleteWithValidByIds(Arrays.asList(ooIds), true) ? 1 : 0);
    }

    /**
     * 查询所有医生姓名
     */
//    @SaCheckPermission("reghos-serv:operation_order:list")
    @GetMapping("/UserNamelist")
    public R<List> getUserNameList() {
        return R.ok(iOperationOrderService.queryAllUserName());
    }
}
