package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.domain.vo.ansible.playbook.PlaybookTags;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.PlaybookUtils;
import com.baiyi.opscloud.domain.generator.OcAnsiblePlaybook;
import com.baiyi.opscloud.domain.vo.ansible.OcAnsiblePlaybookVO;
import com.baiyi.opscloud.domain.vo.ansible.playbook.PlaybookTask;
import com.baiyi.opscloud.facade.ServerTaskFacade;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/4/14 6:26 下午
 * @Version 1.0
 */
@Component
public class AnsiblePlaybookDecorator {

    @Resource
    private ServerTaskFacade serverTaskFacade;

    public OcAnsiblePlaybookVO.AnsiblePlaybook decorator(OcAnsiblePlaybook ocAnsiblePlaybook) {
        OcAnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook = BeanCopierUtils.copyProperties(ocAnsiblePlaybook, OcAnsiblePlaybookVO.AnsiblePlaybook.class);
        PlaybookTags tags = PlaybookUtils.buildTags(ansiblePlaybook.getTags());
        invokeTags(ansiblePlaybook, tags);
        ansiblePlaybook.setTasks(tags.getTasks());

        ansiblePlaybook.setPath(serverTaskFacade.getPlaybookPath(ocAnsiblePlaybook));
        return ansiblePlaybook;
    }

    private void invokeTags(OcAnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook, PlaybookTags tags) {
        Set<String> selectedTags = Sets.newHashSet();
        List<PlaybookTask> tasks = tags.getTasks();
        if(tasks == null || tasks.isEmpty()){
            ansiblePlaybook.setTasks(Lists.newArrayList());
            ansiblePlaybook.setSelectedTags(selectedTags);
            return;
        }

        for (PlaybookTask task : tags.getTasks()) {
            if (task.getChoose() == null)
                task.setChoose(true);
            if (task.getChoose())
                selectedTags.add(task.getTags());
        }
        ansiblePlaybook.setTasks(tags.getTasks());
        ansiblePlaybook.setSelectedTags(selectedTags);
    }

}
