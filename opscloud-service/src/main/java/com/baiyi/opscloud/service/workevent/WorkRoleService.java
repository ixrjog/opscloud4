package com.baiyi.opscloud.service.workevent;

import com.baiyi.opscloud.domain.generator.opscloud.WorkRole;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/8/5 4:25 PM
 * @Since 1.0
 */
public interface WorkRoleService {

    List<WorkRole> queryAll();

    WorkRole getById(Integer id);

    WorkRole getByTag(String tag);

}