package xian.woniuxy.customer.service;

import xian.woniuxy.customer.domain.bo.PatientBo;
import xian.woniuxy.customer.domain.vo.PatientVo;
import xian.woniuxy.domain.Patient;

import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 用户Service接口
 *
 * @author ataoxz
 * @date 2022-08-23
 */
public interface IPatientService {

    /**
     * 查询用户
     */
    PatientVo queryById(Long patientId);

    /**
     * 查询用户列表
     */
    TableDataInfo<PatientVo> queryPageList(PatientBo bo, PageQuery pageQuery);

    /**
     * 查询用户列表
     */
    List<PatientVo> queryList(PatientBo bo);

    /**
     * 修改用户
     */
    Boolean insertByBo(PatientBo bo);

    /**
     * 修改用户
     */
    Boolean updateByBo(PatientBo bo);

    /**
     * 校验并批量删除用户信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
