package xian.woniuxy.reghos.dubboService;

import xian.woniuxy.domain.Medicine;

public interface RemoteMedicineService {

    Medicine queryByMedicineId(Long MedicineId);
}
