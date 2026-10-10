package xian.woniuxy.customer.dubbo;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import xian.woniuxy.customer.dubboService.RemotePresService;
import xian.woniuxy.customer.mapper.PrescriptionMapper;
import xian.woniuxy.domain.MedicineOrder;
import xian.woniuxy.domain.Prescription;

import java.util.List;

/*
 *
 * @author: ren
 * @date:2022/8/23 16:12
 */
@DubboService
@Service
@RequiredArgsConstructor
public class RemotePrescriptionImpl implements RemotePresService {

    @NonNull
    private PrescriptionMapper prescriptionMapper;

    @Override
    public Prescription queryById(Long preId) {
        final Prescription prescription = prescriptionMapper.selectById(preId);
        return prescription;
    }

    @Override
    public List<Prescription> getPres(Long registerId, String patientName) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(registerId != null,"register_id",registerId);
        wrapper.like(patientName != null,"patient_name",patientName);

        return prescriptionMapper.selectList(wrapper);
    }

}
