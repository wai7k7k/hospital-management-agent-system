package xian.woniuxy.reghos.dubboService;

import xian.woniuxy.domain.Bed;

/**
 * @author G
 * @description: TODO
 * @date 2022/8/23 15:02
 */
public interface RemoteBedService {

    Bed selectById(Long bedId);

    boolean updateBedState(Bed bed);
}
