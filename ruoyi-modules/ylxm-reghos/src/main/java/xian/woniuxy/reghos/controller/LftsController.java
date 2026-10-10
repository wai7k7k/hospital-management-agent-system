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
import xian.woniuxy.reghos.domain.vo.LftsVo;
import xian.woniuxy.reghos.domain.bo.LftsBo;
import xian.woniuxy.reghos.service.ILftsService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 肝功能结果控制器
 * 前端访问路由地址为:/reghos-serv/lfts
 *
 * @author G
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/lfts")
public class LftsController extends BaseController {

    private final ILftsService iLftsService;

    /**
     * 查询肝功能结果列表
     */
//    @SaCheckPermission("reghos-serv:lfts:list")
    @GetMapping("/list")
    public TableDataInfo<LftsVo> list(LftsBo bo, PageQuery pageQuery) {
        return iLftsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出肝功能结果列表
     */
//    @SaCheckPermission("reghos-serv:lfts:export")
    @Log(title = "肝功能结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(LftsBo bo, HttpServletResponse response) {
        List<LftsVo> list = iLftsService.queryList(bo);
        ExcelUtil.exportExcel(list, "肝功能结果", LftsVo.class, response);
    }

    /**
     * 获取肝功能结果详细信息
     *
     * @param lftsId 主键
     */
//    @SaCheckPermission("reghos-serv:lfts:query")
    @GetMapping("/{lftsId}")
    public R<LftsVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long lftsId) {
        return R.ok(iLftsService.queryById(lftsId));
    }

    /**
     * 新增肝功能结果
     */
//    @SaCheckPermission("reghos-serv:lfts:add")
    @Log(title = "肝功能结果", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody LftsBo bo) {
        return toAjax(iLftsService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改肝功能结果
     */
//    @SaCheckPermission("reghos-serv:lfts:edit")
    @Log(title = "肝功能结果", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody LftsBo bo) {
        return toAjax(iLftsService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除肝功能结果
     *
     * @param lftsIds 主键串
     */
//    @SaCheckPermission("reghos-serv:lfts:remove")
    @Log(title = "肝功能结果", businessType = BusinessType.DELETE)
    @DeleteMapping("/{lftsIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] lftsIds) {
        return toAjax(iLftsService.deleteWithValidByIds(Arrays.asList(lftsIds), true) ? 1 : 0);
    }
}
