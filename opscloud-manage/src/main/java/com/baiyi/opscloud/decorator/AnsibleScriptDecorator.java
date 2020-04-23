package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnsibleScript;
import com.baiyi.opscloud.domain.vo.ansible.OcAnsibleScriptVO;
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

    public OcAnsibleScriptVO.AnsibleScript decorator(OcAnsibleScript ocAnsibleScript) {
        OcAnsibleScriptVO.AnsibleScript ansibleScript = BeanCopierUtils.copyProperties(ocAnsibleScript, OcAnsibleScriptVO.AnsibleScript.class);
        ansibleScript.setPath(ansibleConfig.getScriptPath(ocAnsibleScript));
        return ansibleScript;
    }

}
