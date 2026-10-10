package xian.woniuxy.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.redis.utils.RedisUtils;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysUser;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xian.woniuxy.customer.mapper.EnterMapper;
import xian.woniuxy.customer.mapper.PatientMapper;
import xian.woniuxy.customer.mapper.RecordMapper;
import xian.woniuxy.customer.service.IEnterService;
import xian.woniuxy.domain.Enter;
import xian.woniuxy.domain.Patient;
import xian.woniuxy.domain.Register;
import xian.woniuxy.domain.Bed;
import xian.woniuxy.domain.bo.EnterBo;
import xian.woniuxy.domain.vo.EnterVo;
import xian.woniuxy.reghos.dubboService.RemoteBedService;


import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 住院;Service业务层处理
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class EnterServiceImpl implements IEnterService {

    private final EnterMapper baseMapper;

    private final RecordMapper recordMapper;

    private final PatientMapper patientMapper;

    @DubboReference
    private final RemoteUserService remoteUserService;

    @DubboReference
    private final RemoteBedService remoteBedService;

    /**
     * 查询住院;
     */
    @Override
    public EnterVo queryById(Long enterId){
        return baseMapper.selectVoById(enterId);
    }

    /**
     * 查询住院;列表
     */
    @Override
    public TableDataInfo<EnterVo> queryPageList(EnterBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Enter> lqw = buildQueryWrapper(bo);
        Page<EnterVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询住院;列表
     */
    @Override
    public List<EnterVo> queryList(EnterBo bo) {
        LambdaQueryWrapper<Enter> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Enter> buildQueryWrapper(EnterBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Enter> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPatientId() != null, Enter::getPatientId, bo.getPatientId());
        lqw.like(StringUtils.isNotBlank(bo.getPatientName()), Enter::getPatientName, bo.getPatientName());
        lqw.eq(bo.getRecordId() != null, Enter::getRecordId, bo.getRecordId());
        lqw.eq(bo.getUserId() != null, Enter::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getUserName()), Enter::getUserName, bo.getUserName());
        lqw.eq(StringUtils.isNotBlank(bo.getDepId()), Enter::getDepId, bo.getDepId());
        lqw.like(StringUtils.isNotBlank(bo.getDepName()), Enter::getDepName, bo.getDepName());
        lqw.eq(bo.getBedId() != null, Enter::getBedId, bo.getBedId());
        lqw.eq(bo.getEnterDate() != null, Enter::getEnterDate, bo.getEnterDate());
        lqw.eq(bo.getOutApply() != null, Enter::getOutApply, bo.getOutApply());
        lqw.eq(bo.getOutData() != null, Enter::getOutData, bo.getOutData());
        lqw.eq(bo.getRegisterId() != null, Enter::getRegisterId, bo.getRegisterId());
        return lqw;
    }

    /**
     * 新增住院;
     */
    @Override
    public Boolean insertByBo(EnterBo bo) {
        Enter add = BeanUtil.toBean(bo, Enter.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setEnterId(add.getEnterId());
        }
        return flag;
    }

    /**
     * 修改住院;
     */
    @Override
    public Boolean updateByBo(EnterBo bo) {
        Enter update = BeanUtil.toBean(bo, Enter.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Enter entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除住院;
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public String goZhuYuan(Long registerId) {
        Register register = RedisUtils.getCacheObject("guahaoregister:" + registerId);
        QueryWrapper<xian.woniuxy.domain.Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_id", register.getPatientId());
        xian.woniuxy.domain.Record record = recordMapper.selectOne(queryWrapper);
        if (record == null) {
            return "no";
        }
        Patient patient = patientMapper.selectById(register.getPatientId());
        SysUser user = remoteUserService.queryByUid(register.getUserId());
        Enter enter = new Enter();
        enter.setPatientId(patient.getPatientId());
        enter.setPatientName(patient.getPatientName());
        enter.setRecordId(record.getRecordId());
        enter.setUserId(user.getUserId());
        enter.setUserName(user.getUserName());
        enter.setDepId(user.getDeptId());
        enter.setDepName(register.getDepName());
        enter.setRegisterId(registerId);
        baseMapper.insert(enter);
        RedisUtils.setCacheObject("entercardid:"+patient.getPatientCardId(), enter, Duration.ofHours(8L));
        return "ok";
    }

    @Override
    public Enter goFBed(Enter enter) {
        Enter enter1 = baseMapper.selectById(enter.getEnterId());
        Patient patient = patientMapper.selectById(enter1.getPatientId());
        enter1.setBedId(enter.getBedId());
        enter1.setEnterDate(new Date());
        baseMapper.updateById(enter1);
        RedisUtils.setCacheObject("entercardid:" + patient.getPatientCardId(), enter1, Duration.ofHours(8));
        Bed bed = remoteBedService.selectById(enter.getBedId());
        bed.setBedState(1L);
        remoteBedService.updateBedState(bed);
        return enter1;
    }

    @Override
    public Enter goChuYuan(Enter enter) {
        Enter enter1 = baseMapper.selectById(enter.getEnterId());
        enter1.setOutData(new Date());
        enter1.setOutApply(2L);
        Patient patient = patientMapper.selectById(enter1.getPatientId());
        RedisUtils.setCacheObject("entercardid:" + patient.getPatientCardId(), enter1, Duration.ofHours(8));
        baseMapper.updateById(enter1);
        Bed bed = remoteBedService.selectById(enter1.getBedId());
        bed.setBedState(0L);
        remoteBedService.updateBedState(bed);
        return enter1;
    }
}
