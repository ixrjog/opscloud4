package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TagMapper extends Mapper<Tag> {

    List<Tag> queryBusinessTagByParam(TagParam.BusinessQuery query);
}