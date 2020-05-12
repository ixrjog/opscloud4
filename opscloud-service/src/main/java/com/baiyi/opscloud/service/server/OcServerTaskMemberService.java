package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerTaskMember;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/8 2:28 下午
 * @Version 1.0
 */
public interface OcServerTaskMemberService {

    void addOcServerTaskMember(OcServerTaskMember ocServerTaskMember);

    void updateOcServerTaskMember(OcServerTaskMember ocServerTaskMember);

    OcServerTaskMember queryOcServerTaskMemberById(int id);

    List<OcServerTaskMember> queryOcServerTaskMemberByTaskStatus(int taskId, String taskStatus, int size);

    int countOcServerTaskMemberByTaskStatus(int taskId, String taskStatus, int size);

    List<OcServerTaskMember> queryOcServerTaskMemberByTaskId(int taskId);
}
