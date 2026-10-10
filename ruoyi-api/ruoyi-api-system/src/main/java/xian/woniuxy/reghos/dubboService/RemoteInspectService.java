package xian.woniuxy.reghos.dubboService;

import xian.woniuxy.domain.Inspect;

/**
 * @author G
 * @description: TODO
 * @date 2022/8/23 15:04
 */
public interface RemoteInspectService {

    Inspect queryByInspectName(String InspectName);


    boolean inster(Inspect inspect);
}
