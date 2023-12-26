package com.baiyi.opscloud.service.task;

import com.baiyi.opscloud.domain.generator.opscloud.ServerTaskMember;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/18 5:31 下午
 * @Version 1.0
 */
public interface ServerTaskMemberService {

    ServerTaskMember getById(Integer id);

    void add(ServerTaskMember serverTaskMember);

    void update(ServerTaskMember serverTaskMember);

    int countByTaskStatus(Integer serverTaskId, String taskStatus);

    int countByFinalized(Integer serverTaskId, boolean finalized);

    List<ServerTaskMember> queryByServerTaskId(Integer serverTaskId);

}