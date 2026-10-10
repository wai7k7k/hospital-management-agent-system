package xian.woniuxy.reghos.dubbo;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import xian.woniuxy.domain.Register;
import xian.woniuxy.reghos.dubboService.RemoteRegisterService;
import xian.woniuxy.reghos.mapper.RegisterMapper;

/**
 * @author: song
 * @date: 2022-08-24 16:40:51
 * @desc: TODO
 */
@DubboService
@Service
@RequiredArgsConstructor
public class RemoteRegisterServiceImpl implements RemoteRegisterService {

    private final RegisterMapper registerMapper;

    @Override
    public Register addRegister(Register register) {
        registerMapper.insert(register);
        return register;
    }
}
