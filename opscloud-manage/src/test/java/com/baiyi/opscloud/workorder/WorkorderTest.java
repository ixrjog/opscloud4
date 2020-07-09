package com.baiyi.opscloud.workorder;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.decorator.department.DepartmentMemberDecorator;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorder;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderApprovalMember;
import com.baiyi.opscloud.domain.vo.org.OrgApprovalVO;
import com.baiyi.opscloud.service.ticket.OcWorkorderApprovalMemberService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.baiyi.opscloud.service.workorder.OcWorkorderService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/30 11:37 上午
 * @Version 1.0
 */
public class WorkorderTest extends BaseUnit {

    @Resource
    private OcWorkorderService ocWorkorderService;

    @Resource
    private OcWorkorderApprovalMemberService ocWorkorderApprovalMemberService;

    @Resource
    private DepartmentMemberDecorator departmentMemberDecorator;

    @Resource
    private OcUserService ocUserService;

    @Test
    void updateUsersUuid() {
        OcWorkorder ocWorkorder = ocWorkorderService.queryOcWorkorderById(19);

        //  ApprovalOptionsVO.ApprovalOptions options = WorkorderUtils.convert(ocWorkorder.getApprovalDetail());

        //System.err.println(JSON.toJSONString(options));

    }

    @Test
    void initTest() {
        // shanma wangzha jiji
        OcUser ocUser = ocUserService.queryOcUserByUsername("jiji");

        OcWorkorderApprovalMember memeber = new OcWorkorderApprovalMember();
        memeber.setGroupId(19);
        memeber.setUserId(ocUser.getId());
        memeber.setUsername(ocUser.getUsername());
        memeber.setComment(ocUser.getDisplayName());


        ocWorkorderApprovalMemberService.addOcWorkorderApprovalMember(memeber);

    }

    @Test
    void deptTest() {
        // banmayu
        OcUser ocUser = ocUserService.queryOcUserByUsername("banmayu");
        OrgApprovalVO.OrgApproval orgApproval = departmentMemberDecorator.decorator(ocUser.getId());
        System.err.println(JSON.toJSONString(orgApproval));

    }
}
