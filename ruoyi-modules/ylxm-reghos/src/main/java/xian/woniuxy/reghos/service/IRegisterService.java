package xian.woniuxy.reghos.service;

import xian.woniuxy.reghos.domain.vo.RegisterVo;
import xian.woniuxy.reghos.domain.bo.RegisterBo;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 挂号Service接口
 *
 * @author ataoxz
 * @date 2022-08-23
 */
public interface IRegisterService {

    /**
     * 查询挂号
     */
    RegisterVo queryById(Long registerId);

    /**
     * 查询挂号列表
     */
    TableDataInfo<RegisterVo> queryPageList(RegisterBo bo, PageQuery pageQuery);

    /**
     * 查询挂号列表
     */
    List<RegisterVo> queryList(RegisterBo bo);

    /**
     * 增加挂号
     */
    Boolean insertByBo(RegisterBo bo);

    /**
     * 修改挂号
     */
    Boolean updateByBo(RegisterBo bo);

    /**
     * 校验并批量删除挂号信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    void endReg(Long registerId);

    void doGuoHao(Long registerId);


    /**
     * 根据患者身份证号查询最近一条挂号记录
     */
    RegisterVo queryByCardId(String cardId);

    //用户挂号

}
