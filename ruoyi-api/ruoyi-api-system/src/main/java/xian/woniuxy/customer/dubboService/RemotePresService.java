package xian.woniuxy.customer.dubboService;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import xian.woniuxy.domain.MedicineOrder;
import xian.woniuxy.domain.Prescription;

import java.sql.Wrapper;
import java.util.List;

/**
 * @author G
 * @description: TODO
 * @date 2022/8/23 15:00
 */
public interface RemotePresService {

    Prescription queryById(Long preId);

    List<Prescription> getPres(Long registerId, String patientName);

}
