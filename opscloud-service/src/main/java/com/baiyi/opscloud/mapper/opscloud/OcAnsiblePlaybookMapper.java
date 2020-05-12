package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAnsiblePlaybook;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAnsiblePlaybookMapper extends Mapper<OcAnsiblePlaybook> {

    List<OcAnsiblePlaybook> queryOcAnsiblePlaybookByParam(AnsiblePlaybookParam.PageQuery pageQuery);
}