package com.baiyi.opscloud.facade.task.impl;

import com.baiyi.opscloud.common.config.OpscloudConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AnsiblePlaybook;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.opscloud.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.opscloud.facade.task.AnsiblePlaybookFacade;
import com.baiyi.opscloud.packer.task.AnsiblePlaybookPacker;
import com.baiyi.opscloud.service.ansible.AnsiblePlaybookService;
import com.google.common.base.Joiner;
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
    private OpscloudConfig opscloudConfig;

    @Resource
    private AnsiblePlaybookService ansiblePlaybookService;

    @Resource
    private AnsiblePlaybookPacker ansiblePlaybookPacker;

    @Override
    public DataTable<AnsiblePlaybookVO.Playbook> queryAnsiblePlaybookPage(AnsiblePlaybookParam.AnsiblePlaybookPageQuery pageQuery) {
        DataTable<AnsiblePlaybook> table = ansiblePlaybookService.queryPageByParam(pageQuery);
        return new DataTable<>(
                table.getData().stream().map(e -> ansiblePlaybookPacker.toVO(e)).collect(Collectors.toList()),
                table.getTotalNum());
    }

    @Override
    public void updateAnsiblePlaybook(AnsiblePlaybookVO.Playbook playbook) {
        AnsiblePlaybook ansiblePlaybook = BeanCopierUtil.copyProperties(playbook, AnsiblePlaybook.class);
        ansiblePlaybookService.update(ansiblePlaybook);
        writeFilePlaybook(ansiblePlaybook);
    }

    @Override
    public void addAnsiblePlaybook(AnsiblePlaybookVO.Playbook playbook) {
        playbook.setPlaybookUuid(IdUtil.buildUUID());
        AnsiblePlaybook ansiblePlaybook = BeanCopierUtil.copyProperties(playbook, AnsiblePlaybook.class);
        ansiblePlaybookService.add(ansiblePlaybook);
        writeFilePlaybook(ansiblePlaybook);
    }

    private void writeFilePlaybook(AnsiblePlaybook ansiblePlaybook) {
        String fileName = Joiner.on(".").join(ansiblePlaybook.getPlaybookUuid(), "yml");
        String path = Joiner.on("/").join(opscloudConfig.getAnsiblePlaybookPath(), fileName);
        IOUtil.writeFile(ansiblePlaybook.getPlaybook(), path);
    }

    @Override
    public void deleteAnsiblePlaybookById(int id) {
        ansiblePlaybookService.deleteById(id);
    }
}
