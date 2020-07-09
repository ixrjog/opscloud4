package com.baiyi.opscloud.decorator.ansible;

import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnsibleScript;
import com.baiyi.opscloud.domain.vo.ansible.AnsibleScriptVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/17 11:13 上午
 * @Version 1.0
 */
@Component
public class AnsibleScriptDecorator {

    @Resource
    private AnsibleConfig ansibleConfig;

    public AnsibleScriptVO.AnsibleScript decorator(OcAnsibleScript ocAnsibleScript) {
        AnsibleScriptVO.AnsibleScript ansibleScript = BeanCopierUtils.copyProperties(ocAnsibleScript, AnsibleScriptVO.AnsibleScript.class);
        ansibleScript.setPath(ansibleConfig.getScriptPath(ocAnsibleScript));
        return ansibleScript;
    }

}
