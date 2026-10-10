package xian.woniuxy.customer.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import xian.woniuxy.customer.domain.bo.PatientBo;
import xian.woniuxy.customer.domain.vo.PatientVo;
import xian.woniuxy.domain.Patient;

/**
 * 用户Mapper接口
 *
 * @author ataoxz
 * @date 2022-08-23
 */
public interface PatientMapper extends BaseMapperPlus<PatientMapper, Patient, PatientVo> {


    Patient selectByPatientName(String patientName);
}
