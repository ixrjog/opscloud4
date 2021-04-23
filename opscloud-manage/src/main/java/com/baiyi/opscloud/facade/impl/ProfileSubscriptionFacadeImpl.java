package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.ansible.factory.ExecutorFactory;
import com.baiyi.opscloud.ansible.impl.AnsiblePlaybookExecutor;
import com.baiyi.opscloud.common.base.ServerTaskType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.profile.ProfileSubscriptionDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcProfileSubscription;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerTask;
import com.baiyi.opscloud.domain.param.profile.ProfileSubscriptionParam;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.opscloud.domain.vo.profile.ProfileSubscriptionVO;
import com.baiyi.opscloud.facade.ProfileSubscriptionFacade;
import com.baiyi.opscloud.facade.ServerBaseFacade;
import com.baiyi.opscloud.service.profile.OcProfileSubscriptionService;
import com.google.common.collect.Sets;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.base.Global.ASYNC_POOL_TASK_COMMON;

/**
 * @Author baiyi
 * @Date 2020/7/9 9:04 上午
 * @Version 1.0
 */
@Service
public class ProfileSubscriptionFacadeImpl implements ProfileSubscriptionFacade {

    @Resource
    private OcProfileSubscriptionService ocProfileSubscriptionService;

    @Resource
    private ProfileSubscriptionDecorator profileSubscriptionDecorator;

    @Override
    public DataTable<ProfileSubscriptionVO.ProfileSubscription> queryProfileSubscriptionPage(ProfileSubscriptionParam.PageQuery pageQuery) {
        DataTable<OcProfileSubscription> table = ocProfileSubscriptionService.queryOcProfileSubscriptionParam(pageQuery);
        List<ProfileSubscriptionVO.ProfileSubscription> page = BeanCopierUtils.copyListProperties(table.getData(), ProfileSubscriptionVO.ProfileSubscription.class);
        return new DataTable<>(page.stream().map(e -> profileSubscriptionDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addProfileSubscription(ProfileSubscriptionVO.ProfileSubscription profileSubscription) {
        OcProfileSubscription ocProfileSubscription = BeanCopierUtils.copyProperties(profileSubscription, OcProfileSubscription.class);
        ocProfileSubscriptionService.addOcProfileSubscription(ocProfileSubscription);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateProfileSubscription(ProfileSubscriptionVO.ProfileSubscription profileSubscription) {
        OcProfileSubscription ocProfileSubscription = BeanCopierUtils.copyProperties(profileSubscription, OcProfileSubscription.class);
        ocProfileSubscriptionService.updateOcProfileSubscription(ocProfileSubscription);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public void publishProfile(int id) {
        OcProfileSubscription ocProfileSubscription = ocProfileSubscriptionService.queryOcProfileSubscriptionById(id);
        ProfileSubscriptionVO.ProfileSubscription profileSubscription
                = profileSubscriptionDecorator.decorator(BeanCopierUtils.copyProperties(ocProfileSubscription, ProfileSubscriptionVO.ProfileSubscription.class));
        ServerTaskExecutorParam.ServerTaskPlaybookExecutor serverTaskPlaybookExecutor = buildTaskExecutorParam(profileSubscription);
        BusinessWrapper<OcServerTask> wrapper = ExecutorFactory.getAnsibleExecutorByKey(AnsiblePlaybookExecutor.COMPONENT_NAME).executor(serverTaskPlaybookExecutor, profileSubscription.getServers());
        if (wrapper.isSuccess()) {
            ocProfileSubscription.setServerTaskId(wrapper.getBody().getId());
            ocProfileSubscription.setExecutionTime(new Date());
            ocProfileSubscriptionService.updateOcProfileSubscription(ocProfileSubscription);
        }
    }

    @Override
    @Async(value = ASYNC_POOL_TASK_COMMON)
    public void publishProfile(String subscriptionType) {
        List<OcProfileSubscription> list = ocProfileSubscriptionService.queryOcProfileSubscriptionBySubscriptionType(subscriptionType);
        list.forEach(e -> publishProfile(e.getId()));
    }

    private ServerTaskExecutorParam.ServerTaskPlaybookExecutor buildTaskExecutorParam(ProfileSubscriptionVO.ProfileSubscription profileSubscription) {
        ServerTaskExecutorParam.ServerTaskPlaybookExecutor taskExecutor = new ServerTaskExecutorParam.ServerTaskPlaybookExecutor();
        taskExecutor.setPlaybookId(profileSubscription.getScriptId());
        taskExecutor.setTaskType(ServerTaskType.PLAYBOOK.getType());
        taskExecutor.setConcurrent(profileSubscription.getServers().size());
        taskExecutor.setVars(profileSubscription.getVars());
        Set<String> hostPatterns = Sets.newHashSet();
        profileSubscription.getServers().forEach(e -> hostPatterns.add(ServerBaseFacade.acqServerName(e)));
        taskExecutor.setHostPatterns(hostPatterns);
        return taskExecutor;
    }


}
