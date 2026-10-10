package xian.woniuxy.customer.dubbo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import xian.woniuxy.customer.dubboService.RemoteEnterService;
import xian.woniuxy.customer.mapper.EnterMapper;
import xian.woniuxy.domain.Enter;


/**
 * @author song
 */
@DubboService
@Service
@RequiredArgsConstructor
public class RemoteEnterServiceImpl implements RemoteEnterService {

    @NonNull
    private EnterMapper enterMapper;


    @Override
    public Enter queryByPatientId(Long patientId) {
        QueryWrapper<Enter> wrapper = new QueryWrapper<>();
        wrapper.eq("patient_id", patientId);
        return enterMapper.selectOne(wrapper);
    }
}
