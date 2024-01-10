package com.baiyi.opscloud.service.ansible;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AnsiblePlaybook;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;

/**
 * @Author baiyi
 * @Date 2021/8/31 5:58 下午
 * @Version 1.0
 */
public interface AnsiblePlaybookService {

    void add(AnsiblePlaybook ansiblePlaybook);

    void update(AnsiblePlaybook ansiblePlaybook);

    void deleteById(int id);

    AnsiblePlaybook getById(int id);

    DataTable<AnsiblePlaybook> queryPageByParam(AnsiblePlaybookParam.AnsiblePlaybookPageQuery pageQuery);

}