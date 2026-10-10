package xian.woniuxy.reghos.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.ResultVO;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.core.validate.QueryGroup;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.excel.utils.ExcelUtil;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.model.XcxLoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xian.woniuxy.domain.Medicine;
import xian.woniuxy.domain.MedicineOrder;
import xian.woniuxy.domain.Prescription;
import xian.woniuxy.reghos.domain.vo.MedicineOrderVo;
import xian.woniuxy.reghos.domain.bo.MedicineOrderBo;
import xian.woniuxy.reghos.service.IMedicineOrderService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 药品订单控制器
 * 前端访问路由地址为:/reghos-serv/medicine_order
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/medicine_order")
public class MedicineOrderController extends BaseController {

    private final IMedicineOrderService iMedicineOrderService;


    /**
     * 查询药品订单列表
     */
//    @SaCheckPermission("reghos-serv:order:list")
    @GetMapping("/list")
    public TableDataInfo<MedicineOrderVo> list(MedicineOrderBo bo, PageQuery pageQuery) {
        return iMedicineOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出药品订单列表
     */
//    @SaCheckPermission("reghos-serv:order:export")
    @Log(title = "药品订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MedicineOrderBo bo, HttpServletResponse response) {
        List<MedicineOrderVo> list = iMedicineOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "药品订单", MedicineOrderVo.class, response);
    }

    /**
     * 获取药品订单详细信息
     *
     * @param moId 主键
     */
//    @SaCheckPermission("reghos-serv:order:query")
    @GetMapping("/{moId}")
    public R<MedicineOrderVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long moId) {
        return R.ok(iMedicineOrderService.queryById(moId));
    }

    /**
     * 新增药品订单
     */
//    @SaCheckPermission("reghos-serv:order:add")
    @Log(title = "药品订单", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MedicineOrder bo) {
        return toAjax(iMedicineOrderService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改药品订单
     */
//    @SaCheckPermission("reghos-serv:order:edit")
    @Log(title = "药品订单", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MedicineOrderBo bo) {
        return toAjax(iMedicineOrderService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除药品订单
     *
     * @param moIds 主键串
     */
//    @SaCheckPermission("reghos-serv:order:remove")
    @Log(title = "药品订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{moIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] moIds) {
        return toAjax(iMedicineOrderService.deleteWithValidByIds(Arrays.asList(moIds), true) ? 1 : 0);
    }


    /**
     * 查询药品订单详情
     */
//    @SaCheckPermission("reghos-serv:order:query")
    @Log(title = "药品订单", businessType = BusinessType.DELETE)
    @GetMapping("details/{moId}")
    public R<MedicineOrderVo> details(@NotNull(message = "主键不能为空") @PathVariable Long moId) {
        final MedicineOrderVo medicineOrderVo = iMedicineOrderService.queryByIdDetails(moId);
        return R.ok(medicineOrderVo);
    }


    @GetMapping("/getPres")
    public ResultVO getPres(@RequestParam(required = false)Long registerId, @RequestParam(required = false) String patientName) {

        List<Prescription> pres = iMedicineOrderService.getPres(registerId, patientName);
        return ResultVO.success().data("prescriptionList",pres);
    }

    @GetMapping("/medicines/{name}")
    public ResultVO getMedicines(@PathVariable String name) {
        List<Medicine> medicines = iMedicineOrderService.getMedicines(name);
        return ResultVO.success().data("medicineList",medicines);
    }


    @PostMapping("/buyMed")
    public ResultVO buyMed(@RequestBody Map<String,MedicineOrder[]> list, @RequestParam Long id) {
        return iMedicineOrderService.buyMed(Arrays.asList(list.get("list")),id) ? ResultVO.success() : ResultVO.failure();
    }
}
