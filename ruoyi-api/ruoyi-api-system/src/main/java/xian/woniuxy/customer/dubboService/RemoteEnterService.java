package xian.woniuxy.customer.dubboService;

import xian.woniuxy.domain.Enter;

/**
 * @author G
 * @description: TODO
 * @date 2022/8/23 15:00
 */
public interface RemoteEnterService {

    Enter queryByPatientId(Long patientId);
}
