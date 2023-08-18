package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.Template;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TemplateMapper extends Mapper<Template> {

    List<String> getKindOptions();

}