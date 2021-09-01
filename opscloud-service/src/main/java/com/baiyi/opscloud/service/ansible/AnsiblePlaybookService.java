package com.baiyi.opscloud.service.ansible;

import com.baiyi.opscloud.domain.generator.opscloud.AnsiblePlaybook;

/**
 * @Author baiyi
 * @Date 2021/8/31 5:58 下午
 * @Version 1.0
 */
public interface AnsiblePlaybookService {

    void add(AnsiblePlaybook ansiblePlaybook);

    void update(AnsiblePlaybook ansiblePlaybook);

    void deleteById(int id);
}
