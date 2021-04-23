package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomain;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunDomainMapper extends Mapper<OcAliyunDomain> {

    List<OcAliyunDomain> queryOcAliyunDomainPage(AliyunDomainParam.PageQuery pageQuery);
}