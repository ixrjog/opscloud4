package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.vo.server.PreviewAttributeVO;
import com.baiyi.opscloud.facade.AttributeFacade;
import com.baiyi.opscloud.factory.attribute.impl.AttributeAnsible;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.task.util.TaskUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.ansible.config.AnsibleConfig.ANSIBLE_HOSTS;
import static com.baiyi.opscloud.task.AttributeTask.TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC;

/**
 * @Author baiyi
 * @Date 2020/4/10 11:28 上午
 * @Version 1.0
 */
@Component
public class AttributeFacadeImpl implements AttributeFacade {

    @Resource
    private AttributeAnsible attributeAnsible;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private AnsibleConfig ansibleConfig;

    @Resource
    private TaskUtil taskUtil;

    @Override
    public void createAnsibleHostsTask() {
        taskUtil.clearSignalCount(TASK_SERVER_ATTRIBUTE_ANSIBLE_TOPIC);
        try {
            String context = attributeAnsible.getHeadInfo();
            List<OcServerGroup> serverGroupList = ocServerGroupService.queryAll();
            for (OcServerGroup serverGroup : serverGroupList) {
                PreviewAttributeVO.PreviewAttribute previewAttribute = attributeAnsible.build(serverGroup);
                if (!StringUtils.isEmpty(previewAttribute.getContent()))
                    context += "\n" + previewAttribute.getContent();
            }
            IOUtils.createFile(ansibleConfig.acqInventoryPath(), ANSIBLE_HOSTS, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
