package com.baiyi.opscloud.packer.task;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AnsiblePlaybook;
import com.baiyi.opscloud.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.opscloud.service.ansible.AnsiblePlaybookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/9/1 11:24 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AnsiblePlaybookPacker {

    private final AnsiblePlaybookService ansiblePlaybookService;

    public void wrap(AnsiblePlaybookVO.IPlaybook iPlaybook) {
        AnsiblePlaybook ansiblePlaybook = ansiblePlaybookService.getById(iPlaybook.getAnsiblePlaybookId());
        if (ansiblePlaybook == null) {
            return;
        }
        iPlaybook.setPlaybook(BeanCopierUtil.copyProperties(ansiblePlaybook, AnsiblePlaybookVO.Playbook.class));
        iPlaybook.setTaskName(ansiblePlaybook.getName());
    }

}
