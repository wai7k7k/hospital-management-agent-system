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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xian.woniuxy.domain.Bed;
import xian.woniuxy.reghos.domain.vo.BedVo;
import xian.woniuxy.reghos.domain.bo.BedBo;
import xian.woniuxy.reghos.service.IBedService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 病床床位;控制器
 * 前端访问路由地址为:/reghos-serv/bed
 *
 * @author G
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/bed")
public class BedController extends BaseController {

    private final IBedService iBedService;

    /**
     * 查询病床床位;列表
     */
//    @SaCheckPermission("reghos-serv:bed:list")
    @GetMapping("/list")
    public TableDataInfo<BedVo> list(BedBo bo, PageQuery pageQuery) {
        return iBedService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出病床床位;列表
     */
//    @SaCheckPermission("reghos-serv:bed:export")
    @Log(title = "病床床位;", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BedBo bo, HttpServletResponse response) {
        List<BedVo> list = iBedService.queryList(bo);
        ExcelUtil.exportExcel(list, "病床床位;", BedVo.class, response);
    }

    /**
     * 获取病床床位;详细信息
     *
     * @param bedId 主键
     */
//    @SaCheckPermission("reghos-serv:bed:query")
    @GetMapping("/{bedId}")
    public R<BedVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long bedId) {
        return R.ok(iBedService.queryById(bedId));
    }

    /**
     * 新增病床床位;
     */
//    @SaCheckPermission("reghos-serv:bed:add")
    @Log(title = "病床床位;", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BedBo bo) {
        return toAjax(iBedService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改病床床位;
     */
//    @SaCheckPermission("reghos-serv:bed:edit")
    @Log(title = "病床床位;", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BedBo bo) {
        return toAjax(iBedService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除病床床位;
     *
     * @param bedIds 主键串
     */
//    @SaCheckPermission("reghos-serv:bed:remove")
    @Log(title = "病床床位;", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bedIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] bedIds) {
        return toAjax(iBedService.deleteWithValidByIds(Arrays.asList(bedIds), true) ? 1 : 0);
    }

    @GetMapping("getBedListByDepId")
    public ResultVO getBedListByDepId(Long depId) {
        List<Bed> bedList = iBedService.getBedListByDepId(depId);
        return ResultVO.success().data("bedList", bedList);
    }
}
