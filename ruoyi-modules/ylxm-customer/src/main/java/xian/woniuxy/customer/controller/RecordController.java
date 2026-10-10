package xian.woniuxy.customer.controller;

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

import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import xian.woniuxy.customer.domain.bo.RecordBo;
import xian.woniuxy.customer.domain.vo.RecordVo;
import xian.woniuxy.customer.service.IRecordService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 病历;病历控制器
 * 前端访问路由地址为:/customer-serv/record
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/record")
public class RecordController extends BaseController {

    private final IRecordService iRecordService;

    /**
     * 查询病历;病历列表
     */
//    @SaCheckPermission("woniuxy:record:list")
    @GetMapping("/list")
    public TableDataInfo<RecordVo> list(RecordBo bo, PageQuery pageQuery) {
        return iRecordService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出病历;病历列表
     */
//    @SaCheckPermission("woniuxy:record:export")
    @Log(title = "病历;病历", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RecordBo bo, HttpServletResponse response) {
        List<RecordVo> list = iRecordService.queryList(bo);
        ExcelUtil.exportExcel(list, "病历;病历", RecordVo.class, response);
    }

    /**
     * 获取病历;病历详细信息
     *
     * @param recordId 主键
     */
//    @SaCheckPermission("woniuxy:record:query")
    @GetMapping("/{recordId}")
    public R<RecordVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long recordId) {
        return R.ok(iRecordService.queryById(recordId));
    }

    /**
     * 新增病历;病历
     */
//    @SaCheckPermission("woniuxy:record:add")
    @Log(title = "病历;病历", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RecordBo bo) {
        return toAjax(iRecordService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改病历;病历
     */
//    @SaCheckPermission("woniuxy:record:edit")
    @Log(title = "病历;病历", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RecordBo bo) {
        return toAjax(iRecordService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除病历;病历
     *
     * @param recordIds 主键串
     */
//    @SaCheckPermission("woniuxy:record:remove")
    @Log(title = "病历;病历", businessType = BusinessType.DELETE)
    @DeleteMapping("/{recordIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] recordIds) {
        return toAjax(iRecordService.deleteWithValidByIds(Arrays.asList(recordIds), true) ? 1 : 0);
    }
}
