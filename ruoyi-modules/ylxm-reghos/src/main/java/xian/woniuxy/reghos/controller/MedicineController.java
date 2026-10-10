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
import xian.woniuxy.reghos.domain.vo.MedicineVo;
import xian.woniuxy.reghos.domain.bo.MedicineBo;
import xian.woniuxy.reghos.service.IMedicineService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 药品控制器
 * 前端访问路由地址为:/reghos-serv/medicine
 *
 * @author ataoxz
 * @date 2022-08-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/medicine")
public class MedicineController extends BaseController {

    private final IMedicineService iMedicineService;

    /**
     * 查询药品列表
     */
//    @SaCheckPermission("reghos-serv:medicine:list")
    @GetMapping("/list")
    public TableDataInfo<MedicineVo> list(MedicineBo bo, PageQuery pageQuery) {
        return iMedicineService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出药品列表
     */
//    @SaCheckPermission("reghos-serv:medicine:export")
    @Log(title = "药品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MedicineBo bo, HttpServletResponse response) {
        List<MedicineVo> list = iMedicineService.queryList(bo);
        ExcelUtil.exportExcel(list, "药品", MedicineVo.class, response);
    }

    /**
     * 获取药品详细信息
     *
     * @param medicineId 主键
     */
//    @SaCheckPermission("reghos-serv:medicine:query")
    @GetMapping("/{medicineId}")
    public R<MedicineVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long medicineId) {
        return R.ok(iMedicineService.queryById(medicineId));
    }

    /**
     * 新增药品
     */
//    @SaCheckPermission("reghos-serv:medicine:add")
    @Log(title = "药品", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MedicineBo bo) {
        return toAjax(iMedicineService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改药品
     */
//    @SaCheckPermission("reghos-serv:medicine:edit")
    @Log(title = "药品", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MedicineBo bo) {
        return toAjax(iMedicineService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除药品
     *
     * @param medicineIds 主键串
     */
//    @SaCheckPermission("reghos-serv:medicine:remove")
    @Log(title = "药品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{medicineIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] medicineIds) {
        return toAjax(iMedicineService.deleteWithValidByIds(Arrays.asList(medicineIds), true) ? 1 : 0);
    }
}
