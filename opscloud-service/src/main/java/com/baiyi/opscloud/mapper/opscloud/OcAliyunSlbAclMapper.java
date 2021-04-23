package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAcl;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunSlbAclMapper extends Mapper<OcAliyunSlbAcl> {
    List<OcAliyunSlbAcl> queryOcAliyunSlbAclPage(AliyunSLBParam.ACLPageQuery pageQuery);
}