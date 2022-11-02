package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.LeoTemplate;
import com.baiyi.opscloud.domain.param.leo.LeoTemplateParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface LeoTemplateMapper extends Mapper<LeoTemplate> {

    List<LeoTemplate> queryTemplateByParam(LeoTemplateParam.TemplatePageQuery pageQuer);

}