package com.baiyi.opscloud.service.ansible;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnsibleScript;
import com.baiyi.opscloud.domain.param.ansible.AnsibleScriptParam;

/**
 * @Author baiyi
 * @Date 2020/4/16 2:53 下午
 * @Version 1.0
 */
public interface OcAnsibleScriptService {

    DataTable<OcAnsibleScript> queryOcAnsibleScriptByParam(AnsibleScriptParam.PageQuery pageQuery);

    OcAnsibleScript queryOcAnsibleScriptById(int id);

    void addOcAnsibleScript(OcAnsibleScript ocAnsiblePlaybook);

    void updateOcAnsibleScript(OcAnsibleScript ocAnsiblePlaybook);

    void deleteOcAnsibleScriptById(int id);
}
