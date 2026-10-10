package xian.woniuxy.reghos.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import xian.woniuxy.reghos.domain.vo.OperationVo;
import xian.woniuxy.domain.Operation;

import java.util.Collection;

/**
 * 手术项目Mapper接口
 *
 * @author ataoxz
 * @date 2022-08-23
 */
public interface OperationMapper extends BaseMapperPlus<OperationMapper, Operation, OperationVo> {

    int updateByIdsoftDel(@Param("ids") Collection<Long> ids);
}
