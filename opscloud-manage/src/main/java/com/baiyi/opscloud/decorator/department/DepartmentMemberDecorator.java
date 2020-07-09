package com.baiyi.opscloud.decorator.department;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartment;
import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartmentMember;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.org.OrgDepartmentMemberVO;
import com.baiyi.opscloud.domain.vo.org.OrgApprovalVO;
import com.baiyi.opscloud.service.org.OcOrgDepartmentMemberService;
import com.baiyi.opscloud.service.org.OcOrgDepartmentService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/4/21 4:42 下午
 * @Version 1.0
 */
@Component
public class DepartmentMemberDecorator {

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcOrgDepartmentMemberService ocOrgDepartmentMemberService;

    @Resource
    private OcOrgDepartmentService ocOrgDepartmentService;

    public OrgDepartmentMemberVO.DepartmentMember decorator(OrgDepartmentMemberVO.DepartmentMember departmentMember) {
        OcUser ocUser = ocUserService.queryOcUserById(departmentMember.getUserId());
        if (ocUser != null) {
            departmentMember.setDisplayName(ocUser.getDisplayName());
            departmentMember.setEmail(ocUser.getEmail());
        }
        return departmentMember;
    }

    // 查询用户在组织架构中的上级
    public OrgApprovalVO.OrgApproval decorator(int userId) {
        // 先查询用户是否有审批权限
        List<OcOrgDepartmentMember> members = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberByUserId(userId);
        // 未加入组织架构
        if (members == null || members.size() == 0)
            return OrgApprovalVO.OrgApproval.builder()
                    .isError(true)
                    .errorMsg(ErrorEnum.ORG_DEPARTMENT_USER_NOT_IN_THE_ORG.getMessage())
                    .build();
        // 查询用户是否有审批权限
        for (OcOrgDepartmentMember member : members) {
            if (member.getIsApprovalAuthority() == 1 || member.getIsLeader() == 1) {
                return OrgApprovalVO.OrgApproval.builder()
                        .isApprovalAuthority(true)
                        .build();
            }
        }
        OrgApprovalVO.OrgApproval orgApproval = queryUserParentByOrg(members);
        if (orgApproval.getIsError()) {
            // 查询上级
            return queryUserSuperiorDepartParentByOrg(members);
        } else {
            return orgApproval;
        }
    }

    private OrgApprovalVO.OrgApproval queryUserSuperiorDepartParentByOrg(List<OcOrgDepartmentMember> members) {
        List<OcOrgDepartmentMember> deptMembers = Lists.newArrayList();
        for (OcOrgDepartmentMember member : members) {
            OcOrgDepartment ocOrgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(member.getDepartmentId());
            if (ocOrgDepartment.getParentId() <= 1)
                continue;
            deptMembers.addAll(ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberByDepartmentId(ocOrgDepartment.getParentId()));
        }
        return queryUserParentByOrg(deptMembers);
    }

    private OrgApprovalVO.OrgApproval queryUserParentByOrg(List<OcOrgDepartmentMember> members) {
        if (members != null && members.size() != 0) {
            // 查询用户在组织架构中拥有审批权的成员信息
            List<OcOrgDepartmentMember> deptMembers = Lists.newArrayList();

            for (OcOrgDepartmentMember member : members) {
                // 部门成员
                deptMembers.addAll(ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberByDepartmentId(member.getDepartmentId()).stream().filter(a ->
                        (a.getIsLeader() == 1 || a.getIsApprovalAuthority() == 1)
                ).collect(Collectors.toList()));
            }
            if (deptMembers.size() != 0) {
                List<OrgDepartmentMemberVO.DepartmentMember> alternativeDeptMembers
                        = BeanCopierUtils.copyListProperties(deptMembers, OrgDepartmentMemberVO.DepartmentMember.class).stream().map(a -> decorator(a)).collect(Collectors.toList());
                OrgApprovalVO.OrgApproval orgApproval = OrgApprovalVO.OrgApproval.builder()
                        .preferenceDeptMember(getPreferenceDeptMember(alternativeDeptMembers))
                        .alternativeDeptMembers(alternativeDeptMembers)
                        .build();
                return orgApproval;
            }
        }
        return OrgApprovalVO.OrgApproval.builder()
                .isError(true)
                .errorMsg(ErrorEnum.ORG_DEPARTMENT_USER_NO_APPROVAL_REATIONSHIP_FOUND.getMessage())
                .build();
    }

    private OrgDepartmentMemberVO.DepartmentMember getPreferenceDeptMember(List<OrgDepartmentMemberVO.DepartmentMember> alternativeDeptMembers) {
        for (OrgDepartmentMemberVO.DepartmentMember member : alternativeDeptMembers) {
            if (member.getIsLeader() == 1)
                return member;
        }
        return null;
    }
}
