package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.Tag;
import com.baiyi.caesar.domain.param.tag.TagParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TagMapper extends Mapper<Tag> {

    List<Tag> queryBusinessTagByParam(TagParam.BusinessQuery query);
}