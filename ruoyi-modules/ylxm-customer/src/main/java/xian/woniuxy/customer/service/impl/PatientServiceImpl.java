package xian.woniuxy.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xian.woniuxy.customer.domain.bo.PatientBo;
import xian.woniuxy.customer.domain.vo.PatientVo;
import xian.woniuxy.customer.mapper.PatientMapper;
import xian.woniuxy.customer.service.IPatientService;

import xian.woniuxy.domain.Patient;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 用户Service业务层处理
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class PatientServiceImpl implements IPatientService {

    private final PatientMapper baseMapper;

    /**
     * 查询用户
     */
    @Override
    public PatientVo queryById(Long patientId){
        return baseMapper.selectVoById(patientId);
    }

    /**
     * 查询用户列表
     */
    @Override
    public TableDataInfo<PatientVo> queryPageList(PatientBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Patient> lqw = buildQueryWrapper(bo);
        Page<PatientVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户列表
     */
    @Override
    public List<PatientVo> queryList(PatientBo bo) {
        LambdaQueryWrapper<Patient> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Patient> buildQueryWrapper(PatientBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Patient> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getPatientPhone()), Patient::getPatientPhone, bo.getPatientPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getPatientPassword()), Patient::getPatientPassword, bo.getPatientPassword());
        lqw.like(StringUtils.isNotBlank(bo.getPatientName()), Patient::getPatientName, bo.getPatientName());
        lqw.eq(StringUtils.isNotBlank(bo.getPatientSex()), Patient::getPatientSex, bo.getPatientSex());
        lqw.eq(StringUtils.isNotBlank(bo.getPatientCardId()), Patient::getPatientCardId, bo.getPatientCardId());
        lqw.eq(StringUtils.isNotBlank(bo.getPatientPhoto()), Patient::getPatientPhoto, bo.getPatientPhoto());
        lqw.eq(bo.getPatientBirthday() != null, Patient::getPatientBirthday, bo.getPatientBirthday());
        lqw.eq(bo.getPatientRegisterDate() != null, Patient::getPatientRegisterDate, bo.getPatientRegisterDate());
        lqw.eq(bo.getPatientMoney() != null, Patient::getPatientMoney, bo.getPatientMoney());
        lqw.eq(StringUtils.isNotBlank(bo.getPatientTest()), Patient::getPatientTest, bo.getPatientTest());
        return lqw;
    }

    /**
     * 新增用户
     */
    @Override
    public Boolean insertByBo(PatientBo bo) {
        Patient add = BeanUtil.toBean(bo, Patient.class);
        add.setPatientMoney(BigDecimal.valueOf(0L));
        add.setPatientPassword("123456");
        add.setPatientRegisterDate(new Date());
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setPatientId(add.getPatientId());
        }
        return flag;
    }

    /**
     * 修改用户
     */
    @Override
    public Boolean updateByBo(PatientBo bo) {
        Patient update = BeanUtil.toBean(bo, Patient.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Patient entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
