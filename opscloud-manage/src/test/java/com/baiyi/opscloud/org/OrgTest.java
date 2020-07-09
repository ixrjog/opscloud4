package com.baiyi.opscloud.org;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.decorator.department.DepartmentMemberDecorator;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.org.DepartmentTreeVO;
import com.baiyi.opscloud.domain.vo.org.OrgApprovalVO;
import com.baiyi.opscloud.facade.OrgFacade;
import com.baiyi.opscloud.service.user.OcUserService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/20 7:49 下午
 * @Version 1.0
 */
public class OrgTest extends BaseUnit {

    @Resource
    private OrgFacade orgFacade;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private DepartmentMemberDecorator departmentMemberDecorator;

    @Test
    void aTest() {
        DepartmentTreeVO.DepartmentTree deptTree = orgFacade.queryDepartmentTree();
        System.err.println(JSON.toJSONString(deptTree));
    }

    @Test
    void bTest() {
        OcUser ocUser = ocUserService.queryOcUserByUsername("baiyi");
        OrgApprovalVO.OrgApproval orgApproval = departmentMemberDecorator.decorator(ocUser.getId());
        System.err.println(JSON.toJSONString(orgApproval));
    }
}
