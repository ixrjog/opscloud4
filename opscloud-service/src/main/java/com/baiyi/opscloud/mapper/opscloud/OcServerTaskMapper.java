package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerTask;
import com.baiyi.opscloud.domain.param.ansible.ServerTaskHistoryParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcServerTaskMapper extends Mapper<OcServerTask> {

    List<OcServerTask> queryOcServerTaskByParam(ServerTaskHistoryParam.PageQuery pageQuery);
}