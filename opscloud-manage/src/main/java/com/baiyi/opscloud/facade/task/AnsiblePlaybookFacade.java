package com.baiyi.opscloud.facade.task;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.opscloud.domain.vo.ansible.AnsiblePlaybookVO;

/**
 * @Author baiyi
 * @Date 2021/9/1 11:08 上午
 * @Version 1.0
 */
public interface AnsiblePlaybookFacade {

    DataTable<AnsiblePlaybookVO.Playbook> queryAnsiblePlaybookPage(AnsiblePlaybookParam.AnsiblePlaybookPageQuery pageQuery);

    void updateAnsiblePlaybook(AnsiblePlaybookVO.Playbook playbook);

    void addAnsiblePlaybook(AnsiblePlaybookVO.Playbook playbook);

    void deleteAnsiblePlaybookById(int id);

}
