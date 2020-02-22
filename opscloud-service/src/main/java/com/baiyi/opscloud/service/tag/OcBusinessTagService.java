package com.baiyi.opscloud.service.tag;

import com.baiyi.opscloud.domain.generator.OcBusinessTag;
import com.baiyi.opscloud.domain.vo.tag.OcBusinessTagVO;

/**
 * @Author baiyi
 * @Date 2020/2/22 9:14 下午
 * @Version 1.0
 */
public interface OcBusinessTagService {

    OcBusinessTag queryOcBusinessTagByUniqueKey(OcBusinessTagVO.BusinessTag businessTag);

    void deleteOcBusinessTagByUniqueKey(OcBusinessTagVO.BusinessTag businessTag);

    void addOcBusinessTag(OcBusinessTag ocBusinessTag);

    void deleteOcBusinessTagById(int id);
}
