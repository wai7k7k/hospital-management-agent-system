package com.ruoyi.system.service;


import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.system.domain.bo.ScheduleBo;
import com.ruoyi.system.domain.vo.ScheduleVo;

import java.util.Collection;
import java.util.List;

/**
 * 排班Service接口
 *
 * @author ataoxz
 * @date 2022-08-22
 */
public interface IScheduleService {

    /**
     * 查询排班
     */
    ScheduleVo queryById(Long scheduleId);

    /**
     * 查询排班列表
     */
    TableDataInfo<ScheduleVo> queryPageList(ScheduleBo bo, PageQuery pageQuery);

    /**
     * 查询排班列表
     */
    List<ScheduleVo> queryList(ScheduleBo bo);

    /**
     * 修改排班
     */
    Boolean insertByBo(ScheduleBo bo);

    /**
     * 修改排班
     */
    Boolean updateByBo(ScheduleBo bo);

    /**
     * 校验并批量删除排班信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
