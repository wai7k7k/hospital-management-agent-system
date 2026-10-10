package com.ruoyi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.redis.utils.RedisUtils;
import com.ruoyi.system.api.domain.Schedule;
import com.ruoyi.system.api.domain.SysDept;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.bo.ScheduleBo;
import com.ruoyi.system.domain.vo.ScheduleVo;
import com.ruoyi.system.mapper.ScheduleMapper;
import com.ruoyi.system.service.IScheduleService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 排班Service业务层处理
 *
 * @author ataoxz
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements IScheduleService {

    private final ScheduleMapper baseMapper;

    private final SysUserMapper sysUserMapper;

    private final SysDeptMapper sysDeptMapper;

    /**
     * 查询排班
     */
    @Override
    public ScheduleVo queryById(Long scheduleId){
        return baseMapper.selectVoById(scheduleId);
    }

    /**
     * 查询排班列表
     */
    @Override
    public TableDataInfo<ScheduleVo> queryPageList(ScheduleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Schedule> lqw = buildQueryWrapper(bo);
        Page<ScheduleVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<ScheduleVo> scheduleVos = result.getRecords();
        scheduleVos.forEach(scheduleVo -> {
            Object o = RedisUtils.getCacheObject("guahaonum:" + scheduleVo.getScheduleId());
            if (o == null) {
                scheduleVo.setShengyu(scheduleVo.getScheduleNum());
            } else {
                Long n = Long.valueOf(o.toString());
                scheduleVo.setShengyu(scheduleVo.getScheduleNum() - n);
            }
        });
        return TableDataInfo.build(result);
    }

    /**
     * 查询排班列表
     */
    @Override
    public List<ScheduleVo> queryList(ScheduleBo bo) {
        LambdaQueryWrapper<Schedule> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Schedule> buildQueryWrapper(ScheduleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Schedule> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, Schedule::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getUserName()), Schedule::getUserName, bo.getUserName());
        lqw.eq(bo.getDepId() != null, Schedule::getDepId, bo.getDepId());
        lqw.like(StringUtils.isNotBlank(bo.getDepName()), Schedule::getDepName, bo.getDepName());
        lqw.eq(StringUtils.isNotBlank(bo.getAvatar()), Schedule::getAvatar, bo.getAvatar());
        lqw.eq(bo.getScheduleDate() != null, Schedule::getScheduleDate, bo.getScheduleDate());
        lqw.eq(StringUtils.isNotBlank(bo.getScheduleTime()), Schedule::getScheduleTime, bo.getScheduleTime());
        lqw.eq(bo.getScheduleNum() != null, Schedule::getScheduleNum, bo.getScheduleNum());
        return lqw;
    }

    /**
     * 新增排班
     */
    @Override
    public Boolean insertByBo(ScheduleBo bo) {
        Schedule add = BeanUtil.toBean(bo, Schedule.class);
        Long userId = bo.getUserId();
        Long depId = bo.getDepId();
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        SysDept sysDept = sysDeptMapper.selectVoById(depId);
        add.setAvatar(sysUser.getAvatar());
        add.setDepName(sysDept.getDeptName());
        add.setUserName(sysUser.getUserName());
        validEntityBeforeSave(add);
        return baseMapper.insert(add) > 0;
    }

    /**
     * 修改排班
     */
    @Override
    public Boolean updateByBo(ScheduleBo bo) {
        Schedule update = BeanUtil.toBean(bo, Schedule.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Schedule entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除排班
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
