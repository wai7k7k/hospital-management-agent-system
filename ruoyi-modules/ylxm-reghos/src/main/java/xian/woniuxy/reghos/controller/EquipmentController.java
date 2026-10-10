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
import xian.woniuxy.reghos.domain.vo.EquipmentVo;
import xian.woniuxy.reghos.domain.bo.EquipmentBo;
import xian.woniuxy.reghos.service.IEquipmentService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 设备控制器
 * 前端访问路由地址为:/reghos-serv/equipment
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/equipment")
public class EquipmentController extends BaseController {

    private final IEquipmentService iEquipmentService;

    /**
     * 查询设备列表
     */
//    @SaCheckPermission("reghos-serv:equipment:list")
    @GetMapping("/list")
    public TableDataInfo<EquipmentVo> list(EquipmentBo bo, PageQuery pageQuery) {
        return iEquipmentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出设备列表
     */
//    @SaCheckPermission("reghos-serv:equipment:export")
    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(EquipmentBo bo, HttpServletResponse response) {
        List<EquipmentVo> list = iEquipmentService.queryList(bo);
        ExcelUtil.exportExcel(list, "设备", EquipmentVo.class, response);
    }

    /**
     * 获取设备详细信息
     *
     * @param equipmentId 主键
     */
//    @SaCheckPermission("reghos-serv:equipment:query")
    @GetMapping("/{equipmentId}")
    public R<EquipmentVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long equipmentId) {
        return R.ok(iEquipmentService.queryById(equipmentId));
    }

    /**
     * 新增设备
     */
//    @SaCheckPermission("reghos-serv:equipment:add")
    @Log(title = "设备", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody EquipmentBo bo) {
        return toAjax(iEquipmentService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改设备
     */
//    @SaCheckPermission("reghos-serv:equipment:edit")
    @Log(title = "设备", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody EquipmentBo bo) {
        return toAjax(iEquipmentService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除设备
     *
     * @param equipmentIds 主键串
     */
//    @SaCheckPermission("reghos-serv:equipment:remove")
    @Log(title = "设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/{equipmentIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] equipmentIds) {
        return toAjax(iEquipmentService.deleteWithValidByIds(Arrays.asList(equipmentIds), true) ? 1 : 0);
    }
}
