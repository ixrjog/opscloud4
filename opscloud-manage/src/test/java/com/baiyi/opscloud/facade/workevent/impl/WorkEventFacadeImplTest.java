package com.baiyi.opscloud.facade.workevent.impl;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.WorkRole;
import com.baiyi.opscloud.facade.workevent.WorkEventFacade;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
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
        SessionUtil.setUsername("xiuyuan");
        List<WorkRole> workRoles = workEventFacade.queryMyWorkRole();
        print(workRoles);
    }

}