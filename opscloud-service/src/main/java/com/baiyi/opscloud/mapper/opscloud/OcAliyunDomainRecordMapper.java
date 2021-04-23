package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomainRecord;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainRecordParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunDomainRecordMapper extends Mapper<OcAliyunDomainRecord> {

    List<OcAliyunDomainRecord> queryOcAliyunDomainRecordPage(AliyunDomainRecordParam.PageQuery pageQuery);
}