package com.ruoyi.system.dubbo;

import com.ruoyi.system.api.RemoteDeptService;
import com.ruoyi.system.api.domain.SysDept;
import com.ruoyi.system.mapper.SysDeptMapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:zhangzhao
 * @Date 2022/08/24 11:22:30
 * @Description: TODO
 */
@RequiredArgsConstructor
@DubboService
@Service
public class RemoteDeptServiceImpl implements RemoteDeptService {

    private final SysDeptMapper sysDeptMapper;
    @Override
    public List<SysDept> deptList() {
        return sysDeptMapper.selectList(null);
    }

    @Override
    public int addDept(SysDept sysDept) {
        return sysDeptMapper.insert(sysDept);
    }

    @Override
    public SysDept selectByDeptName(String depName) {
        return sysDeptMapper.selectBydeptName(depName);
    }


}
