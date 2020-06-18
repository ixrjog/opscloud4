package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcTag;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcTagMapper extends Mapper<OcTag> {

    List<OcTag> queryOcTagByParam(TagParam.PageQuery pageQuery);

    /**
     * 查询业务相关标签
     * @param businessQuery
     * @return
     */
    List<OcTag> queryOcTagByBusinessParam(TagParam.BusinessQuery businessQuery);

    /**
     * 查询业务不相关标签
     * @param businessQuery
     * @return
     */
    List<OcTag> queryOcTagNotInByBusinessParam(TagParam.BusinessQuery businessQuery);

}