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
import xian.woniuxy.reghos.domain.vo.BloodRoutineVo;
import xian.woniuxy.reghos.domain.bo.BloodRoutineBo;
import xian.woniuxy.reghos.service.IBloodRoutineService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 血常规结果;控制器
 * 前端访问路由地址为:/reghos-serv/routine
 *
 * @author G
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/routine")
public class BloodRoutineController extends BaseController {


    private final IBloodRoutineService iBloodRoutineService;

    /**
     * 查询血常规结果;列表
     */
//    @SaCheckPermission("reghos-serv:routine:list")
    @GetMapping("/list")
    public TableDataInfo<BloodRoutineVo> list(BloodRoutineBo bo, PageQuery pageQuery) {
        return iBloodRoutineService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出血常规结果;列表
     */
//    @SaCheckPermission("reghos-serv:routine:export")
    @Log(title = "血常规结果;", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BloodRoutineBo bo, HttpServletResponse response) {
        List<BloodRoutineVo> list = iBloodRoutineService.queryList(bo);
        ExcelUtil.exportExcel(list, "血常规结果;", BloodRoutineVo.class, response);
    }

    /**
     * 获取血常规结果;详细信息
     *
     * @param brId 主键
     */
//    @SaCheckPermission("reghos-serv:routine:query")
    @GetMapping("/{brId}")
    public R<BloodRoutineVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long brId) {
        return R.ok(iBloodRoutineService.queryById(brId));
    }

    /**
     * 新增血常规结果;
     */
//    @SaCheckPermission("reghos-serv:routine:add")
    @Log(title = "血常规结果;", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BloodRoutineBo bo) {
        return toAjax(iBloodRoutineService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改血常规结果;
     */
//    @SaCheckPermission("reghos-serv:routine:edit")
    @Log(title = "血常规结果;", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BloodRoutineBo bo) {
        return toAjax(iBloodRoutineService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除血常规结果;
     *
     * @param brIds 主键串
     */
//    @SaCheckPermission("reghos-serv:routine:remove")
    @Log(title = "血常规结果;", businessType = BusinessType.DELETE)
    @DeleteMapping("/{brIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] brIds) {
        return toAjax(iBloodRoutineService.deleteWithValidByIds(Arrays.asList(brIds), true) ? 1 : 0);
    }
}
