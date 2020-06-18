package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunLogMember;
import com.baiyi.opscloud.domain.param.cloud.AliyunLogMemberParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunLogMemberMapper extends Mapper<OcAliyunLogMember> {

    List<OcAliyunLogMember> queryOcAliyunLogMemberByParam(AliyunLogMemberParam.PageQuery pageQuery);
}