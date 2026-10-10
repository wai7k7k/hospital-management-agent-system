package xian.woniuxy.reghos.dubbo;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import xian.woniuxy.domain.Inspect;
import xian.woniuxy.reghos.domain.vo.InspectVo;
import xian.woniuxy.reghos.dubboService.RemoteInspectService;
import xian.woniuxy.reghos.mapper.InspectMapper;

import javax.validation.constraints.Negative;

/*
 *
 * @author: ren
 * @date:2022/8/24 11:54
 */
@RequiredArgsConstructor
@DubboService
@Service
public class RemoteInspectServiceImpl implements RemoteInspectService {

    @NonNull
    private InspectMapper inspectMapper;


    @Override
    public Inspect queryByInspectName(String InspectName) {
        Wrapper wrapper = new LambdaQueryWrapper();
        wrapper.equals(InspectName);
        return inspectMapper.selectOne(wrapper);
    }

    @Override
    public boolean inster(Inspect inspect) {
        return inspectMapper.insert(inspect) > 0 ? true : false;
    }
}
