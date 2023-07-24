package com.baiyi.opscloud.facade.workevent.impl;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.generator.opscloud.WorkRole;
import com.baiyi.opscloud.facade.workevent.WorkEventFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/8/23 13:40
 * @Version 1.0
 */
class WorkEventFacadeImplTest extends BaseUnit {

    @Resource
    private WorkEventFacade workEventFacade;

    @Test
    void queryMyWorkRoleTest() {
        SessionHolder.setUsername("xiuyuan");
        List<WorkRole> workRoles = workEventFacade.queryMyWorkRole();
        print(workRoles);
    }

}