package com.baiyi.opscloud.facade.task.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AnsiblePlaybook;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.opscloud.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.opscloud.facade.task.AnsiblePlaybookFacade;
import com.baiyi.opscloud.packer.task.AnsiblePlaybookPacker;
import com.baiyi.opscloud.service.ansible.AnsiblePlaybookService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/9/1 11:08 上午
 * @Version 1.0
 */
@Component
public class AnsiblePlaybookFacadeImpl implements AnsiblePlaybookFacade {

    @Resource
    private AnsiblePlaybookService ansiblePlaybookService;

    @Resource
    private AnsiblePlaybookPacker ansiblePlaybookPakcer;

    @Override
    public DataTable<AnsiblePlaybookVO.Playbook> queryAnsiblePlaybookPage(AnsiblePlaybookParam.AnsiblePlaybookPageQuery pageQuery) {
        DataTable<AnsiblePlaybook> table = ansiblePlaybookService.queryPageByParam(pageQuery);
        return new DataTable<>(
                table.getData().stream().map(e -> ansiblePlaybookPakcer.toVO(e)).collect(Collectors.toList()),
                table.getTotalNum());
    }
}
