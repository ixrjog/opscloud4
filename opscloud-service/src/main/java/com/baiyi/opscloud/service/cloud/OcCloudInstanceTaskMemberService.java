package com.baiyi.opscloud.service.cloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceTaskMember;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/30 4:37 下午
 * @Version 1.0
 */
public interface OcCloudInstanceTaskMemberService {

    List<OcCloudInstanceTaskMember> queryOcCloudInstanceTaskMemberByTaskId(int taskId);

    List<OcCloudInstanceTaskMember> queryOcCloudInstanceTaskMemberByTaskIdAndPhase(int taskId,String taskPhase);

    /**
     * 统计条目
     *
     * @param taskId
     * @return
     */
    int countOcCloudInstanceTaskMemberByTaskId(int taskId);

    void addOcCloudInstanceTaskMember(OcCloudInstanceTaskMember ocCloudInstanceTaskMember);

    void updateOcCloudInstanceTaskMember(OcCloudInstanceTaskMember ocCloudInstanceTaskMember);
}
