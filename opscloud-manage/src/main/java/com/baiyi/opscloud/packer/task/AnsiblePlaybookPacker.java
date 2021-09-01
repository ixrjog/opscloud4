package com.baiyi.opscloud.packer.task;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AnsiblePlaybook;
import com.baiyi.opscloud.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.opscloud.packer.base.AbstractPacker;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/9/1 11:24 上午
 * @Version 1.0
 */
@Component
public class AnsiblePlaybookPacker extends AbstractPacker<AnsiblePlaybookVO.Playbook, AnsiblePlaybook> {

    @Override
    public AnsiblePlaybookVO.Playbook toVO(AnsiblePlaybook ansiblePlaybook) {
        return BeanCopierUtil.copyProperties(ansiblePlaybook, AnsiblePlaybookVO.Playbook.class);
    }


}
