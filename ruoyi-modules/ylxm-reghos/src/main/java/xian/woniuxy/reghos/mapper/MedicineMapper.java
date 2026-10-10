package xian.woniuxy.reghos.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;

import org.apache.ibatis.annotations.Param;
import xian.woniuxy.domain.Medicine;


import org.apache.ibatis.annotations.Param;

import xian.woniuxy.domain.Medicine;


import xian.woniuxy.reghos.domain.vo.MedicineVo;

import java.util.Collection;

/**
 * 药品Mapper接口
 *
 * @author ataoxz
 * @date 2022-08-22
 */
public interface MedicineMapper extends BaseMapperPlus<MedicineMapper, Medicine, MedicineVo> {

    int updateByIdsoftDel(@Param("ids") Collection<Long> ids);

    int insertExample(Medicine add);
}
