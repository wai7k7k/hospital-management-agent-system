package com.ruoyi.system.dubbo;

import com.ruoyi.system.api.RemoteScheduleService;
import com.ruoyi.system.api.domain.Schedule;
import com.ruoyi.system.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author: song
 * @date: 2022-08-25 09:46:00
 * @desc: TODO
 */
@RequiredArgsConstructor
@DubboService
@Service
public class RemoteScheduleServiceImpl implements RemoteScheduleService {
    private final ScheduleMapper scheduleMapper;
    @Override
    public Schedule queryById(Long scheduleId) {
        return scheduleMapper.selectById(scheduleId);
    }
}
