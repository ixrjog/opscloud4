package com.baiyi.opscloud.decorator.profile;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.IDUtils;
import com.baiyi.opscloud.decorator.ansible.AnsiblePlaybookDecorator;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnsiblePlaybook;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.vo.profile.ProfileSubscriptionVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.ServerBaseFacade;
import com.baiyi.opscloud.factory.attribute.impl.AnsibleAttribute;
import com.baiyi.opscloud.service.ansible.OcAnsiblePlaybookService;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/7/9 9:15 上午
 * @Version 1.0
 */
@Component
public class ProfileSubscriptionDecorator {

    @Resource
    private AnsibleAttribute ansibleAttribute;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private AnsiblePlaybookDecorator ansiblePlaybookDecorator;

    @Resource
    private OcAnsiblePlaybookService ocAnsiblePlaybookService;

    public ProfileSubscriptionVO.ProfileSubscription decorator(ProfileSubscriptionVO.ProfileSubscription profileSubscription) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(profileSubscription.getServerGroupId());
        profileSubscription.setServerGroup(BeanCopierUtils.copyProperties(ocServerGroup, ServerGroupVO.ServerGroup.class));
        Map<String, List<OcServer>> serverMap = ansibleAttribute.grouping(ocServerGroup, true);
        profileSubscription.setServers(getServersByHostPattern(serverMap, profileSubscription.getHostPattern()));
        if (!IDUtils.isEmpty(profileSubscription.getScriptId())) {
            OcAnsiblePlaybook ocAnsiblePlaybook = ocAnsiblePlaybookService.queryOcAnsiblePlaybookById(profileSubscription.getScriptId());
            profileSubscription.setAnsiblePlaybook(ansiblePlaybookDecorator.decorator(ocAnsiblePlaybook));
        }
        return profileSubscription;
    }

    private List<ServerVO.Server> getServersByHostPattern(Map<String, List<OcServer>> serverMap, String hostPattern) {
        if (serverMap.containsKey(hostPattern))
            return BeanCopierUtils.copyListProperties(serverMap.get(hostPattern), ServerVO.Server.class);
        List<ServerVO.Server> servers = Lists.newArrayList();
        for (String key : serverMap.keySet()) {
            for (OcServer ocServer : serverMap.get(key)) {
                if (ServerBaseFacade.acqServerName(ocServer).equals(hostPattern)) {
                    servers.add(BeanCopierUtils.copyProperties(ocServer, ServerVO.Server.class));
                    return servers;
                }
            }
        }
        return servers;
    }

}
