package com.baiyi.opscloud.decorator.cloud;

import com.baiyi.opscloud.common.base.CloudInstanceTaskPhase;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceTaskMember;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTaskVO;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTaskMemberService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;



/**
 * @Author baiyi
 * @Date 2020/3/31 11:09 上午
 * @Version 1.0
 */
@Component
public class CloudInstanceTaskDecorator {

    @Resource
    private OcCloudInstanceTaskMemberService ocCloudInstanceTaskMemberService;

    public CloudInstanceTaskVO.CloudInstanceTask decorator(CloudInstanceTaskVO.CloudInstanceTask cloudInstanceTask) {
        List<OcCloudInstanceTaskMember> memberList = ocCloudInstanceTaskMemberService.queryOcCloudInstanceTaskMemberByTaskId(cloudInstanceTask.getId());
        Map<String, List<OcCloudInstanceTaskMember>> memberMap = Maps.newHashMap();
        for (OcCloudInstanceTaskMember member : memberList) {
            if (memberMap.containsKey(member.getTaskPhase())) {
                memberMap.get(member.getTaskPhase()).add(member);
            } else {
                List<OcCloudInstanceTaskMember> list = Lists.newArrayList();
                list.add(member);
                memberMap.put(member.getTaskPhase(), list);
            }
        }
        cloudInstanceTask.setMemberMap(memberMap);
        // 计算完成百分比
        try {
            int cp = memberMap.get(CloudInstanceTaskPhase.FINALIZED.getPhase()).size() * 100 / cloudInstanceTask.getCreateSize();
            cloudInstanceTask.setCompletedPercentage(cp);
        } catch (Exception e) {
            cloudInstanceTask.setCompletedPercentage(0);
        }

        return cloudInstanceTask;
    }
}