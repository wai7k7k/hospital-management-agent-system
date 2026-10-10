package xian.woniuxy.reghos.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;

import org.springframework.stereotype.Service;
import xian.woniuxy.customer.dubboService.RemotePatientService;
import xian.woniuxy.customer.dubboService.RemotePresService;
import xian.woniuxy.domain.Medicine;
import xian.woniuxy.domain.Patient;
import xian.woniuxy.domain.Prescription;
import xian.woniuxy.reghos.domain.bo.MedicineOrderBo;
import xian.woniuxy.reghos.domain.vo.MedicineOrderVo;
import xian.woniuxy.domain.MedicineOrder;
import xian.woniuxy.reghos.dubboService.RemoteMedicineService;
import xian.woniuxy.reghos.mapper.MedicineMapper;
import xian.woniuxy.reghos.mapper.MedicineOrderMapper;
import xian.woniuxy.reghos.service.IMedicineOrderService;
import xian.woniuxy.reghos.service.IMedicineService;

import java.math.BigDecimal;
import java.util.*;


/**
 * 药品订单Service业务层处理
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class MedicineOrderServiceImpl implements IMedicineOrderService {


    private final MedicineOrderMapper baseMapper;

    @DubboReference
    private final RemotePresService remotePresService;

    @DubboReference
    private RemoteUserService remoteUserService;

    @DubboReference
    private RemotePatientService remotePatientService;

    @DubboReference
    private RemoteMedicineService remoteMedicineService;

    @NonNull
    private MedicineMapper medicineMapper;


    /**
     * 查询药品订单
     */
    @Override
    public MedicineOrderVo queryById(Long moId) {
        return baseMapper.selectVoById(moId);
    }

    /**
     * 查询药品订单列表
     */
    @Override
    public TableDataInfo<MedicineOrderVo> queryPageList(MedicineOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MedicineOrder> lqw = buildQueryWrapper(bo);
        Page<MedicineOrderVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        final List<Medicine> medicines = medicineMapper.selectList();
        Map<Long,String> map = new HashMap<>();
        medicines.forEach(medicine->{map.put(medicine.getMedicineId(),medicine.getMedicineName());});
        result.getRecords().forEach(record->{record.setMedicineName(map.get(record.getMedicineId()));});
        return TableDataInfo.build(result);
    }

    /**
     * 查询药品订单列表
     */
    @Override
    public List<MedicineOrderVo> queryList(MedicineOrderBo bo) {
        LambdaQueryWrapper<MedicineOrder> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MedicineOrder> buildQueryWrapper(MedicineOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MedicineOrder> lqw = Wrappers.lambdaQuery();
        lqw.like(bo.getMoId() != null, MedicineOrder::getMoId, bo.getMoId());
        return lqw;
    }

    /**
     * 新增药品订单
     */
    @Override
    public Boolean insertByBo(MedicineOrder bo) {

//        MedicineOrder add = BeanUtil.toBean(bo, MedicineOrder.class);
//        validEntityBeforeSave(add);
        final Long medicineId = bo.getMedicineId();
        final Medicine medicine = remoteMedicineService.queryByMedicineId(medicineId);
        bo.setMedicinePrice(medicine.getMedicinePrice());
        bo.setMoTotalPrice(medicine.getMedicinePrice().multiply(new BigDecimal(bo.getMedicineNum())));
        bo.setMoDate(new Date());
        boolean flag = baseMapper.insert(bo) > 0;
//        boolean flag = baseMapper.insert(add) > 0;
//        if (flag) {
//            bo.setMoId(add.getMoId());
//        }
        return flag;
    }

    /**
     * 修改药品订单
     */
    @Override
    public Boolean updateByBo(MedicineOrderBo bo) {
        MedicineOrder update = BeanUtil.toBean(bo, MedicineOrder.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(MedicineOrder entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除药品订单
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public MedicineOrderVo queryByIdDetails(Long moId) {

        final MedicineOrderVo medicineOrderVo = baseMapper.selectVoById(moId);

        final Long prescriptionId = medicineOrderVo.getPrescriptionId();
        final Prescription prescription = remotePresService.queryById(prescriptionId);
        final Long userId = prescription.getUserId();
        final Long patientId = prescription.getPatientId();
        final SysUser user = remoteUserService.queryByUid(userId);
        final Patient patient = remotePatientService.queryById(patientId);
        final Medicine medicine = remoteMedicineService.queryByMedicineId(medicineOrderVo.getMedicineId());
        medicineOrderVo.setMedicineName(medicine.getMedicineName());
        medicineOrderVo.setNickName(user.getNickName());
        medicineOrderVo.setPatientName(patient.getPatientName());

        return medicineOrderVo;
    }

    @Override
    public List<Prescription> getPres(Long registerId, String patientName) {
        return remotePresService.getPres(registerId, patientName);
    }

    @Override
    public List<Medicine> getMedicines(String name) {
        return medicineMapper.selectList(new QueryWrapper<Medicine>().like("medicine_name",name).eq("soft_del",1));
    }

    @Override
    public Boolean buyMed(List<MedicineOrder> list, Long id) {
        for (MedicineOrder order:list) {
            order.setPrescriptionId(id);
            order.setMoTotalPrice(order.getMedicinePrice().multiply(new BigDecimal(order.getMedicineNum())));
            order.setMoDate(new Date());
        }
        return baseMapper.insertBatch(list);
    }

}
