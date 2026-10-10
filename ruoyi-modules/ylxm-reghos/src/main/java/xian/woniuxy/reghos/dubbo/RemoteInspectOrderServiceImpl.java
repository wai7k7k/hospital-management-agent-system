package xian.woniuxy.reghos.dubbo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import xian.woniuxy.domain.InspectOrder;
import xian.woniuxy.reghos.dubboService.RemoteInspectOrderService;
import xian.woniuxy.reghos.mapper.InspectOrderMapper;

/*
 *
 * @author: ren
 * @date:2022/8/24 12:18
 */
@DubboService
@Service
@RequiredArgsConstructor
public class RemoteInspectOrderServiceImpl implements RemoteInspectOrderService {

    @NonNull
    private InspectOrderMapper inspectOrderMapper;

    @Override
    public boolean inster(InspectOrder inspectOrder) {
        final int insert = inspectOrderMapper.insert(inspectOrder);
        return insert > 0 ? false : true;
    }
}
