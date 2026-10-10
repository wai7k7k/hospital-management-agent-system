package xian.woniuxy.reghos.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xian.woniuxy.reghos.domain.bo.MedicineBo;
import xian.woniuxy.reghos.domain.vo.MedicineVo;
import xian.woniuxy.domain.Medicine;
import xian.woniuxy.reghos.mapper.MedicineMapper;
import xian.woniuxy.reghos.service.IMedicineService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 药品Service业务层处理
 *
 * @author ataoxz
 * @date 2022-08-22
 */
@RequiredArgsConstructor
@Service
public class MedicineServiceImpl implements IMedicineService {

    @NonNull
    private final MedicineMapper medicineMapper;

    /**
     * 查询药品
     */
    @Override
    public MedicineVo queryById(Long medicineId){
        return medicineMapper.selectVoById(medicineId);
    }

    /**
     * 查询药品列表
     */
    @Override
    public TableDataInfo<MedicineVo> queryPageList(MedicineBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Medicine> lqw = buildQueryWrapper(bo);
        Page<MedicineVo> result = medicineMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询药品列表
     */
    @Override
    public List<MedicineVo> queryList(MedicineBo bo) {
        LambdaQueryWrapper<Medicine> lqw = buildQueryWrapper(bo);
        return medicineMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Medicine> buildQueryWrapper(MedicineBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Medicine> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getMedicineName()), Medicine::getMedicineName, bo.getMedicineName());
        lqw.eq(true,Medicine::getSoftDel, 1);
        lqw.eq(bo.getRecipel() != null, Medicine::getRecipel, bo.getRecipel());
        return lqw;
    }

    /**
     * 新增药品
     */
    @Override
    public Boolean insertByBo(MedicineBo bo) {
        Medicine add = BeanUtil.toBean(bo, Medicine.class);
        validEntityBeforeSave(add);
        boolean flag = medicineMapper.insertExample(add) >0;
       // boolean flag = medicineMapper.insert(add) > 0;
        if (flag) {
            bo.setMedicineId(add.getMedicineId());
        }
        return flag;
    }

    /**
     * 修改药品
     */
    @Override
    public Boolean updateByBo(MedicineBo bo) {
        Medicine update = BeanUtil.toBean(bo, Medicine.class);
        validEntityBeforeSave(update);
        return medicineMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Medicine entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除药品
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return medicineMapper.updateByIdsoftDel(ids) > 0;
    }
}
