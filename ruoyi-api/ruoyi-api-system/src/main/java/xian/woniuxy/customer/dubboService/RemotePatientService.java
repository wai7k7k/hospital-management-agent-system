package xian.woniuxy.customer.dubboService;

import xian.woniuxy.domain.Patient;

import java.util.List;

/**
 * @author G
 * @description: TODO
 * @date 2022/8/23 15:00
 */
public interface RemotePatientService {

    Patient queryById(Long patientId);

    Patient queryByPatientName(String patientName);


    Patient queryByCardId(String cardId);

    /** 按手机号查患者（登录用） */
    Patient queryByPhone(String phone);
}
