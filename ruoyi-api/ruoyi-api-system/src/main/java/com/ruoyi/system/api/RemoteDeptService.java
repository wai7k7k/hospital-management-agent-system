package com.ruoyi.system.api;

import com.ruoyi.system.api.domain.SysDept;

import java.util.List;

public interface RemoteDeptService {
    List<SysDept> deptList();
    int addDept(SysDept sysDept);
    SysDept selectByDeptName(String depName);

}
