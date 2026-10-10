package xian.woniuxy.customer.service.impl;

import com.ruoyi.common.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import xian.woniuxy.customer.service.CustomerService;
import xian.woniuxy.domain.Register;
import xian.woniuxy.reghos.dubboService.RemoteRegisterService;

/**
 * @author: song
 * @date: 2022-08-24 20:12:15
 * @desc: TODO
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    @DubboReference
    private final RemoteRegisterService remoteRegisterService;
    @Override
    public Register selfRegister(Register register) {
        // TODO 客户挂号逻辑
        return remoteRegisterService.addRegister(register);
    }
}
