package com.baiyi.opscloud.packer.task;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AnsiblePlaybook;
import com.baiyi.opscloud.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.opscloud.packer.base.AbstractPacker;
import com.baiyi.opscloud.service.ansible.AnsiblePlaybookService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/9/1 11:24 上午
 * @Version 1.0
 */
@Component
public class AnsiblePlaybookPacker extends AbstractPacker<AnsiblePlaybookVO.Playbook, AnsiblePlaybook> {

    @Resource
    private AnsiblePlaybookService ansiblePlaybookService;

    @Override
    public AnsiblePlaybookVO.Playbook toVO(AnsiblePlaybook ansiblePlaybook) {
        return BeanCopierUtil.copyProperties(ansiblePlaybook, AnsiblePlaybookVO.Playbook.class);
    }

    public void wrap(AnsiblePlaybookVO.IPlaybook iPlaybook) {
        AnsiblePlaybook ansiblePlaybook = ansiblePlaybookService.getById(iPlaybook.getAnsiblePlaybookId());
        if (ansiblePlaybook == null) return;
        iPlaybook.setPlaybook(toVO(ansiblePlaybook));
        iPlaybook.setTaskName(ansiblePlaybook.getName());
    }

}
