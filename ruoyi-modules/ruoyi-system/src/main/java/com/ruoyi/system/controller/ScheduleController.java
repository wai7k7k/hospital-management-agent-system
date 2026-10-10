package com.ruoyi.system.controller;

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
import com.ruoyi.system.domain.vo.ScheduleVo;
import com.ruoyi.system.domain.bo.ScheduleBo;
import com.ruoyi.system.service.IScheduleService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 排班控制器
 * 前端访问路由地址为:/system/schedule
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
public class ScheduleController extends BaseController {

    private final IScheduleService iScheduleService;

    /**
     * 查询排班列表
     */
//    @SaCheckPermission("system:schedule:list")  ← AI 服务需直调，暂时开放
    @GetMapping("/list")
    public TableDataInfo<ScheduleVo> list(ScheduleBo bo, PageQuery pageQuery) {
        return iScheduleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出排班列表
     */
    @SaCheckPermission("system:schedule:export")
    @Log(title = "排班", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ScheduleBo bo, HttpServletResponse response) {
        List<ScheduleVo> list = iScheduleService.queryList(bo);
        ExcelUtil.exportExcel(list, "排班", ScheduleVo.class, response);
    }

    /**
     * 获取排班详细信息
     *
     * @param scheduleId 主键
     */
//    @SaCheckPermission("system:schedule:query")
    @GetMapping("/{scheduleId}")
    public R<ScheduleVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long scheduleId) {
        return R.ok(iScheduleService.queryById(scheduleId));
    }

    /**
     * 新增排班
     */
    @SaCheckPermission("system:schedule:add")
    @Log(title = "排班", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ScheduleBo bo) {
        return toAjax(iScheduleService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改排班
     */
    @SaCheckPermission("system:schedule:edit")
    @Log(title = "排班", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ScheduleBo bo) {
        return toAjax(iScheduleService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除排班
     *
     * @param scheduleIds 主键串
     */
    @SaCheckPermission("system:schedule:remove")
    @Log(title = "排班", businessType = BusinessType.DELETE)
    @DeleteMapping("/{scheduleIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] scheduleIds) {
        return toAjax(iScheduleService.deleteWithValidByIds(Arrays.asList(scheduleIds), true) ? 1 : 0);
    }
}
