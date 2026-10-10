package xian.woniuxy.reghos.dubbo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import xian.woniuxy.domain.Medicine;
import xian.woniuxy.reghos.dubboService.RemoteMedicineService;
import xian.woniuxy.reghos.mapper.MedicineMapper;

/*
 *
 * @author: ren
 * @date:2022/8/24 16:47
 */
@DubboService
@Service
@RequiredArgsConstructor
public class RemoteMedicineServiceImple implements RemoteMedicineService {

    @NonNull
    private MedicineMapper medicineMapper;


    @Override
    public Medicine queryByMedicineId(Long MedicineId) {
        return medicineMapper.selectById(MedicineId);
    }
}
