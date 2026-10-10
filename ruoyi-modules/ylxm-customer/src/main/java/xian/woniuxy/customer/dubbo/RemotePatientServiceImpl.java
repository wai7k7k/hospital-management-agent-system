package xian.woniuxy.customer.dubbo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import xian.woniuxy.customer.dubboService.RemotePatientService;
import xian.woniuxy.customer.mapper.PatientMapper;
import xian.woniuxy.domain.Patient;

/*
 *
 * @author: ren
 * @date:2022/8/23 17:14
 */
@DubboService
@Service
@RequiredArgsConstructor
public class RemotePatientServiceImpl implements RemotePatientService {

    @NonNull
    private PatientMapper patientMapper;

    @Override
    public Patient queryById(Long patientId) {
        return patientMapper.selectById(patientId);
    }

    @Override
    public Patient queryByPatientName(String patientName) {
        return patientMapper.selectByPatientName(patientName);
    }

    @Override
    public Patient queryByCardId(String cardId) {
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_card_id", cardId);
        return patientMapper.selectOne(queryWrapper);
    }

    @Override
    public Patient queryByPhone(String phone) {
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_phone", phone);
        return patientMapper.selectOne(queryWrapper);
    }
}
