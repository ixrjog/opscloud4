package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.domain.generator.OcCloudInstanceTaskMember;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTaskVO;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTaskMemberService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.baiyi.opscloud.facade.impl.CloudInstanceTaskFacadeImpl.TASK_STATUS_RUNNING;

/**
 * @Author baiyi
 * @Date 2020/3/31 11:09 上午
 * @Version 1.0
 */
@Component
public class CloudInstanceTaskDecorator {

    @Resource
    private OcCloudInstanceTaskMemberService ocCloudInstanceTaskMemberService;

    public OcCloudInstanceTaskVO.CloudInstanceTask decorator(OcCloudInstanceTaskVO.CloudInstanceTask cloudInstanceTask) {
        List<OcCloudInstanceTaskMember> memberList = ocCloudInstanceTaskMemberService.queryOcCloudInstanceTaskMemberByTaskId(cloudInstanceTask.getId());
        Map<String, List<OcCloudInstanceTaskMember>> memberMap = Maps.newHashMap();
        for (OcCloudInstanceTaskMember member : memberList) {
            if (memberMap.containsKey(member.getTaskStatus())) {
                memberMap.get(member.getTaskStatus()).add(member);
            } else {
                List<OcCloudInstanceTaskMember> list = Lists.newArrayList();
                list.add(member);
                memberMap.put(member.getTaskStatus(), list);
            }
        }
        cloudInstanceTask.setMemberMap(memberMap);
        // 计算完成百分比
        try {
            int cp = memberMap.get(TASK_STATUS_RUNNING).size() * 100 / cloudInstanceTask.getCreateSize();
            cloudInstanceTask.setCompletedPercentage(cp);
        } catch (Exception e) {
            cloudInstanceTask.setCompletedPercentage(0);
        }

        return cloudInstanceTask;
    }
}