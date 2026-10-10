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
import com.ruoyi.common.redis.utils.RedisUtils;
import com.ruoyi.common.satoken.utils.LoginHelper;
import com.ruoyi.system.api.RemoteDeptService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xian.woniuxy.domain.Register;
import xian.woniuxy.domain.Patient;
import xian.woniuxy.customer.dubboService.RemotePatientService;
import xian.woniuxy.reghos.domain.vo.RegisterVo;
import xian.woniuxy.reghos.domain.bo.RegisterBo;
import xian.woniuxy.reghos.service.IRegisterService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.time.Duration;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 挂号控制器
 * 前端访问路由地址为:/reghos-serv/register
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/register")
public class RegisterController extends BaseController {

    private final IRegisterService iRegisterService;
    @DubboReference
    private final RemotePatientService remotePatientService;

    /**
     * 查询挂号列表
     */
//    @SaCheckPermission("reghos-serv:register:list")
    @GetMapping("/list")
    public TableDataInfo<RegisterVo> list(RegisterBo bo, PageQuery pageQuery) {
        return iRegisterService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出挂号列表
     */
//    @SaCheckPermission("reghos-serv:register:export")
    @Log(title = "挂号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RegisterBo bo, HttpServletResponse response) {
        List<RegisterVo> list = iRegisterService.queryList(bo);
        ExcelUtil.exportExcel(list, "挂号", RegisterVo.class, response);
    }

    /**
     * 获取挂号详细信息
     *
     * @param registerId 主键
     */
//    @SaCheckPermission("reghos-serv:register:query")
    @GetMapping("/{registerId}")
    public R<RegisterVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long registerId) {
        return R.ok(iRegisterService.queryById(registerId));
    }

    /**
     * 新增挂号
     */
//    @SaCheckPermission("reghos-serv:register:add")
    @Log(title = "挂号", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RegisterBo bo) {
        return toAjax(iRegisterService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改挂号
     */
//    @SaCheckPermission("reghos-serv:register:edit")
    @Log(title = "挂号", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RegisterBo bo) {
        return toAjax(iRegisterService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除挂号
     *
     * @param registerIds 主键串
     */
//    @SaCheckPermission("reghos-serv:register:remove")
    @Log(title = "挂号", businessType = BusinessType.DELETE)
    @DeleteMapping("/{registerIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] registerIds) {
        return toAjax(iRegisterService.deleteWithValidByIds(Arrays.asList(registerIds), true) ? 1 : 0);
    }

    @GetMapping("getRegisterByCardId")
    public ResultVO getRegisterByCardId(String cardId) {
        Object cacheObject = RedisUtils.getCacheObject("guahaopatient:" + cardId);
        if (cacheObject == null) {
            // Redis 未命中，从 DB 查询
            RegisterVo vo = iRegisterService.queryByCardId(cardId);
            if (vo != null) {
                // 补齐患者姓名
                if (vo.getPatientName() == null && vo.getPatientId() != null) {
                    Patient patient = remotePatientService.queryById(vo.getPatientId());
                    if (patient != null) {
                        vo.setPatientName(patient.getPatientName());
                    }
                }
                return ResultVO.success().data("register", vo);
            }
            return ResultVO.success("no");
        }
        // Redis 命中 — 但可能是旧缓存（patientName=null），需要修复
        if (cacheObject instanceof Register) {
            Register reg = (Register) cacheObject;
            if (reg.getPatientName() == null && reg.getPatientId() != null) {
                Patient patient = remotePatientService.queryById(reg.getPatientId());
                if (patient != null) {
                    reg.setPatientName(patient.getPatientName());
                    RedisUtils.setCacheObject("guahaopatient:" + cardId, reg, Duration.ofHours(5));
                }
            }
            return ResultVO.success().data("register", reg);
        }
        return ResultVO.success().data("register", cacheObject);
    }

    @GetMapping("getRegisterById")
    public ResultVO getRegisterById(String registerId) {
        Object cacheObject = RedisUtils.getCacheObject("guahaoregister:" + registerId);
        if (cacheObject == null) {
            return ResultVO.success("no");
        }
        return ResultVO.success().data("register", cacheObject);
    }

    /**
     * 模糊搜索挂号记录（供检查订单弹窗检索回填使用）
     * GET /register/search?keyword=xxx
     */
    @GetMapping("search")
    public ResultVO search(@RequestParam String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResultVO.success("no");
        }
        keyword = keyword.trim();
        if (keyword.matches("\\d+")) {
            Long registerId = Long.parseLong(keyword);
            RegisterVo vo = iRegisterService.queryById(registerId);
            if (vo != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("registerId", vo.getRegisterId());
                result.put("patientId", vo.getPatientId());
                result.put("patientName", vo.getPatientName());
                result.put("depId", vo.getDepId());
                result.put("depName", vo.getDepName());
                result.put("doctorId", vo.getUserId());
                result.put("doctorName", vo.getUserName());
                return ResultVO.success().data("data", result);
            }
        } else {
            List<RegisterVo> list = iRegisterService.queryList(new RegisterBo());
            for (RegisterVo vo : list) {
                if (vo.getPatientName() != null && vo.getPatientName().contains(keyword)) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("registerId", vo.getRegisterId());
                    result.put("patientId", vo.getPatientId());
                    result.put("patientName", vo.getPatientName());
                    result.put("depId", vo.getDepId());
                    result.put("depName", vo.getDepName());
                    result.put("doctorId", vo.getUserId());
                    result.put("doctorName", vo.getUserName());
                    return ResultVO.success().data("data", result);
                }
            }
        }
        return ResultVO.success("no");
    }

    @GetMapping("queryRegisterListByUser")
    public ResultVO queryRegisterListByUser() {
        Long userId = LoginHelper.getUserId();
        List<Register> registerList = RedisUtils.getCacheList("guahaolist:" + userId);
        return ResultVO.success().data("registerList", registerList);
    }

    @GetMapping("endReg")
    public ResultVO endReg(Long registerId) {
        iRegisterService.endReg(registerId);
        return ResultVO.success();
    }

    @GetMapping("doGuoHao")
    public ResultVO doGuoHao(Long registerId) {
        iRegisterService.doGuoHao(registerId);
        return ResultVO.success();
    }
}
