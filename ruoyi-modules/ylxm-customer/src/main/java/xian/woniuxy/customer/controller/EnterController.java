package xian.woniuxy.customer.controller;

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
import com.ruoyi.common.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xian.woniuxy.customer.service.IEnterService;
import xian.woniuxy.domain.Enter;
import xian.woniuxy.domain.vo.EnterVo;
import xian.woniuxy.domain.bo.EnterBo;

import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 住院;控制器
 * 前端访问路由地址为:/customer-serv/enter
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enter")
public class EnterController extends BaseController {

    private final IEnterService iEnterService;

    /**
     * 查询住院;列表
     */
//    @SaCheckPermission("woniuxy:enter:list")
    @GetMapping("/list")
    public TableDataInfo<EnterVo> list(EnterBo bo, PageQuery pageQuery) {
        return iEnterService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出住院;列表
     */
//    @SaCheckPermission("woniuxy:enter:export")
    @Log(title = "住院;", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(EnterBo bo, HttpServletResponse response) {
        List<EnterVo> list = iEnterService.queryList(bo);
        ExcelUtil.exportExcel(list, "住院;", EnterVo.class, response);
    }

    /**
     * 获取住院;详细信息
     *
     * @param enterId 主键
     */
//    @SaCheckPermission("woniuxy:enter:query")
    @GetMapping("/{enterId}")
    public R<EnterVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long enterId) {
        return R.ok(iEnterService.queryById(enterId));
    }

    /**
     * 新增住院;
     */
//    @SaCheckPermission("woniuxy:enter:add")
    @Log(title = "住院;", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody EnterBo bo) {
        return toAjax(iEnterService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改住院;
     */
//    @SaCheckPermission("woniuxy:enter:edit")
    @Log(title = "住院;", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody EnterBo bo) {
        return toAjax(iEnterService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除住院;
     *
     * @param enterIds 主键串
     */
//    @SaCheckPermission("woniuxy:enter:remove")
    @Log(title = "住院;", businessType = BusinessType.DELETE)
    @DeleteMapping("/{enterIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] enterIds) {
        return toAjax(iEnterService.deleteWithValidByIds(Arrays.asList(enterIds), true) ? 1 : 0);
    }

    @GetMapping("goZhuYuan")
    public ResultVO goZhuYuan(Long registerId) {
        String ss = iEnterService.goZhuYuan(registerId);
        return ResultVO.success(ss);
    }

    @GetMapping("getEnterByCardId")
    public ResultVO getEnterByCardId(String cardId) {
        Enter enter = RedisUtils.getCacheObject("entercardid:" + cardId);
        return enter == null ? ResultVO.success("no") : ResultVO.success().data("enter", enter);
    }

    @PostMapping("goFBed")
    public ResultVO goFBed(@RequestBody Enter enter) {
        Enter enter1 = iEnterService.goFBed(enter);
        return ResultVO.success().data("enter", enter1);
    }

    @PostMapping("goChuYuan")
    public ResultVO goChuYuan(@RequestBody Enter enter) {
        Enter enter1 = iEnterService.goChuYuan(enter);
        return ResultVO.success().data("enter", enter1);
    }
}
