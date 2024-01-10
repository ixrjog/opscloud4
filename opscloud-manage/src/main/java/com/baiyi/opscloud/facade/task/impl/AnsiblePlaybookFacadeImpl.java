package com.baiyi.opscloud.facade.task.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AnsiblePlaybook;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.opscloud.domain.vo.ansible.AnsiblePlaybookVO;
import com.baiyi.opscloud.facade.task.AnsiblePlaybookFacade;
import com.baiyi.opscloud.service.ansible.AnsiblePlaybookService;
import com.baiyi.opscloud.util.PlaybookUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/1 11:08 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AnsiblePlaybookFacadeImpl implements AnsiblePlaybookFacade {

    private final AnsiblePlaybookService ansiblePlaybookService;

    @Override
    public DataTable<AnsiblePlaybookVO.Playbook> queryAnsiblePlaybookPage(AnsiblePlaybookParam.AnsiblePlaybookPageQuery pageQuery) {
        DataTable<AnsiblePlaybook> table = ansiblePlaybookService.queryPageByParam(pageQuery);
        List<AnsiblePlaybookVO.Playbook> data = BeanCopierUtil.copyListProperties(table.getData(), AnsiblePlaybookVO.Playbook.class);
        return new DataTable<>(
                data,
                table.getTotalNum());
    }

    @Override
    public void updateAnsiblePlaybook(AnsiblePlaybookParam.Playbook playbook) {
        AnsiblePlaybook ansiblePlaybook = BeanCopierUtil.copyProperties(playbook, AnsiblePlaybook.class);
        ansiblePlaybookService.update(ansiblePlaybook);
        writeFilePlaybook(ansiblePlaybook);
    }

    @Override
    public void addAnsiblePlaybook(AnsiblePlaybookParam.Playbook playbook) {
        playbook.setPlaybookUuid(IdUtil.buildUUID());
        AnsiblePlaybook ansiblePlaybook = BeanCopierUtil.copyProperties(playbook, AnsiblePlaybook.class);
        ansiblePlaybookService.add(ansiblePlaybook);
        writeFilePlaybook(ansiblePlaybook);
    }

    private void writeFilePlaybook(AnsiblePlaybook ansiblePlaybook) {
        String path = PlaybookUtil.toPath(ansiblePlaybook);
        IOUtil.writeFile(ansiblePlaybook.getPlaybook(), path);
    }

    @Override
    public void deleteAnsiblePlaybookById(int id) {
        ansiblePlaybookService.deleteById(id);
    }

}