package com.baiyi.opscloud.service.ansible;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnsiblePlaybook;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;

/**
 * @Author baiyi
 * @Date 2020/4/13 4:38 下午
 * @Version 1.0
 */
public interface OcAnsiblePlaybookService {

    DataTable<OcAnsiblePlaybook> queryOcAnsiblePlaybookByParam(AnsiblePlaybookParam.PageQuery pageQuery);

    OcAnsiblePlaybook queryOcAnsiblePlaybookById(int id);

    void addOcAnsiblePlaybook(OcAnsiblePlaybook ocAnsiblePlaybook);

    void updateOcAnsiblePlaybook(OcAnsiblePlaybook ocAnsiblePlaybook);

    void deleteOcAnsiblePlaybookById(int id);
}
