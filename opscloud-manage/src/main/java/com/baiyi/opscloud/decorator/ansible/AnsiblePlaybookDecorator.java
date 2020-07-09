package com.baiyi.opscloud.decorator.ansible;

import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.PlaybookUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnsiblePlaybook;
import com.baiyi.opscloud.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.opscloud.domain.vo.ansible.playbook.PlaybookTags;
import com.baiyi.opscloud.domain.vo.ansible.playbook.PlaybookTask;
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
    private AnsibleConfig ansibleConfig;

    public AnsiblePlaybookVO.AnsiblePlaybook decorator(OcAnsiblePlaybook ocAnsiblePlaybook) {
        AnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook = BeanCopierUtils.copyProperties(ocAnsiblePlaybook, AnsiblePlaybookVO.AnsiblePlaybook.class);
        try{
            PlaybookTags tags = PlaybookUtils.buildTags(ansiblePlaybook.getTags());
            invokeTags(ansiblePlaybook, tags);
            ansiblePlaybook.setTasks(tags.getTasks());
            ansiblePlaybook.setFormatError(false);
        }catch (Exception e){
            ansiblePlaybook.setFormatError(true);
        }

        ansiblePlaybook.setPath(ansibleConfig.getPlaybookPath(ocAnsiblePlaybook));
        return ansiblePlaybook;
    }

    private void invokeTags(AnsiblePlaybookVO.AnsiblePlaybook ansiblePlaybook, PlaybookTags tags) {
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
