package com.baiyi.opscloud.service.ticket;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderApprovalMember;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/6 4:00 下午
 * @Version 1.0
 */
public interface OcWorkorderApprovalMemberService {

    List<OcWorkorderApprovalMember> queryOcWorkorderApprovalMemberByGroupId(int groupId);

    void addOcWorkorderApprovalMember(OcWorkorderApprovalMember ocWorkorderApprovalMember);

    void updateOcWorkorderApprovalMember(OcWorkorderApprovalMember ocWorkorderApprovalMember);

    void deleteOcWorkorderApprovalMemberById(int id);
}
