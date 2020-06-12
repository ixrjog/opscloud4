package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcBusinessTag;
import com.baiyi.opscloud.domain.vo.tag.BusinessTagVO;
import tk.mybatis.mapper.common.Mapper;

public interface OcBusinessTagMapper extends Mapper<OcBusinessTag> {

    OcBusinessTag queryOcBusinessTagByUniqueKey(BusinessTagVO.BusinessTag businessTag);

    int deleteOcBusinessTagByUniqueKey(BusinessTagVO.BusinessTag businessTag);
}