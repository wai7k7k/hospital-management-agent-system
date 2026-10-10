package xian.woniuxy.reghos.dubbo;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import xian.woniuxy.domain.Bed;
import xian.woniuxy.reghos.dubboService.RemoteBedService;
import xian.woniuxy.reghos.mapper.BedMapper;

/**
 * @author: song
 * @date: 2022-08-28 20:10:27
 * @desc: TODO
 */
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteBedServiceImpl implements RemoteBedService {

    private final BedMapper bedMapper;

    @Override
    public Bed selectById(Long bedId) {
        return bedMapper.selectById(bedId);
    }

    @Override
    public boolean updateBedState(Bed bed) {
        return bedMapper.updateById(bed) > 0;
    }
}
