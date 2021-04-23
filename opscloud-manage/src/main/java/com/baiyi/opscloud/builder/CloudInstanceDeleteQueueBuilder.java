package com.baiyi.opscloud.builder;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceDeleteQueue;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceTaskMember;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author <a href="mailto:xujian@gegejia.com">修远</a>
 * @Date 2020/8/19 11:13 AM
 * @Since 1.0
 */
public class CloudInstanceDeleteQueueBuilder {

    public static OcCloudInstanceDeleteQueue build(OcCloudInstanceTaskMember ocCloudInstanceTaskMember) {
        OcCloudInstanceDeleteQueue deleteQueue = new OcCloudInstanceDeleteQueue();
        deleteQueue.setTaskId(ocCloudInstanceTaskMember.getTaskId());
        deleteQueue.setInstanceId(ocCloudInstanceTaskMember.getInstanceId());
        deleteQueue.setTaskStatus(2);
        return deleteQueue;
    }

    public static List<OcCloudInstanceDeleteQueue> buildList(List<OcCloudInstanceTaskMember> ocCloudInstanceTaskMemberList) {
        List<OcCloudInstanceDeleteQueue> deleteQueueList = Lists.newArrayListWithCapacity(ocCloudInstanceTaskMemberList.size());
        ocCloudInstanceTaskMemberList.forEach(x -> deleteQueueList.add(build(x)));
        return deleteQueueList;
    }
}
