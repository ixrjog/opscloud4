package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAnsibleScript;
import com.baiyi.opscloud.domain.param.ansible.AnsibleScriptParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAnsibleScriptMapper extends Mapper<OcAnsibleScript> {

    List<OcAnsibleScript> queryOcAnsibleScriptByParam(AnsibleScriptParam.PageQuery pageQuery);
}