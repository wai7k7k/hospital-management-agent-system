package xian.woniuxy.reghos.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import xian.woniuxy.domain.Register;
import xian.woniuxy.reghos.domain.vo.RegisterVo;
import com.ruoyi.system.api.domain.Schedule;
import java.util.List;
import java.util.Map;

/**
 * 挂号Mapper接口
 *
 * @author ataoxz
 * @date 2022-08-23
 */
public interface RegisterMapper extends BaseMapperPlus<RegisterMapper, Register, RegisterVo> {

    @Select("SELECT dep_id, dep_name, soft_del FROM dep WHERE dep_name = #{depName} AND soft_del = 1 LIMIT 1")
    @Results({
        @Result(property = "depId", column = "dep_id"),
        @Result(property = "depName", column = "dep_name"),
        @Result(property = "softDel", column = "soft_del")
    })
    Map<String, Object> selectDepByName(@Param("depName") String depName);

    @Select("SELECT * FROM schedule WHERE dep_id = #{depId} "
          + "AND schedule_date = #{scheduleDate} AND schedule_time = #{scheduleTime}")
    List<Schedule> selectSchedulesByDepAndDate(
        @Param("depId") Long depId,
        @Param("scheduleDate") String scheduleDate,
        @Param("scheduleTime") String scheduleTime
    );

    @Select("SELECT * FROM schedule WHERE schedule_id = #{scheduleId}")
    Schedule selectScheduleById(@Param("scheduleId") Long scheduleId);
}
