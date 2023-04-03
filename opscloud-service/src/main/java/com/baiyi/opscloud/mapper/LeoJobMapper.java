package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface LeoJobMapper extends Mapper<LeoJob> {

    List<LeoJob> queryPageByParam(LeoJobParam.JobPageQuery pageQuery);

}