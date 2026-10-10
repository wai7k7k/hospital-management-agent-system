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
import xian.woniuxy.reghos.domain.vo.BloodGlucoseVo;
import xian.woniuxy.reghos.domain.bo.BloodGlucoseBo;
import xian.woniuxy.reghos.service.IBloodGlucoseService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 血糖结果;控制器
 * 前端访问路由地址为:/reghos-serv/glucose
 *
 * @author G
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/glucose")
public class BloodGlucoseController extends BaseController {

    private final IBloodGlucoseService iBloodGlucoseService;

    /**
     * 查询血糖结果;列表
     */
//    @SaCheckPermission("reghos-serv:glucose:list")
    @GetMapping("/list")
    public TableDataInfo<BloodGlucoseVo> list(BloodGlucoseBo bo, PageQuery pageQuery) {
        return iBloodGlucoseService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出血糖结果;列表
     */
//    @SaCheckPermission("reghos-serv:glucose:export")
    @Log(title = "血糖结果;", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BloodGlucoseBo bo, HttpServletResponse response) {
        List<BloodGlucoseVo> list = iBloodGlucoseService.queryList(bo);
        ExcelUtil.exportExcel(list, "血糖结果;", BloodGlucoseVo.class, response);
    }

    /**
     * 获取血糖结果;详细信息
     *
     * @param bgId 主键
     */
//    @SaCheckPermission("reghos-serv:glucose:query")
    @GetMapping("/{bgId}")
    public R<BloodGlucoseVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long bgId) {
        return R.ok(iBloodGlucoseService.queryById(bgId));
    }

    /**
     * 新增血糖结果;
     */
//    @SaCheckPermission("reghos-serv:glucose:add")
    @Log(title = "血糖结果;", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BloodGlucoseBo bo) {
        return toAjax(iBloodGlucoseService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改血糖结果;
     */
//    @SaCheckPermission("reghos-serv:glucose:edit")
    @Log(title = "血糖结果;", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BloodGlucoseBo bo) {
        return toAjax(iBloodGlucoseService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除血糖结果;
     *
     * @param bgIds 主键串
     */
//    @SaCheckPermission("reghos-serv:glucose:remove")
    @Log(title = "血糖结果;", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bgIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] bgIds) {
        return toAjax(iBloodGlucoseService.deleteWithValidByIds(Arrays.asList(bgIds), true) ? 1 : 0);
    }
}
