package xian.woniuxy.reghos.service;

import xian.woniuxy.reghos.domain.vo.EquipmentVo;
import xian.woniuxy.reghos.domain.bo.EquipmentBo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 设备Service接口
 *
 * @author ataoxz
 * @date 2022-08-23
 */
public interface IEquipmentService {

    /**
     * 查询设备
     */
    EquipmentVo queryById(Long equipmentId);

    /**
     * 查询设备列表
     */
    TableDataInfo<EquipmentVo> queryPageList(EquipmentBo bo, PageQuery pageQuery);

    /**
     * 查询设备列表
     */
    List<EquipmentVo> queryList(EquipmentBo bo);

    /**
     * 修改设备
     */
    Boolean insertByBo(EquipmentBo bo);

    /**
     * 修改设备
     */
    Boolean updateByBo(EquipmentBo bo);

    /**
     * 校验并批量删除设备信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
