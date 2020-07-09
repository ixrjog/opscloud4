package com.baiyi.opscloud.service.tag;

import com.baiyi.opscloud.domain.generator.opscloud.OcBusinessTag;
import com.baiyi.opscloud.domain.vo.tag.BusinessTagVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/22 9:14 下午
 * @Version 1.0
 */
public interface OcBusinessTagService {

    OcBusinessTag queryOcBusinessTagByUniqueKey(BusinessTagVO.BusinessTag businessTag);

    List<OcBusinessTag> queryOcBusinessTagByBusinessTypeAndBusinessId(int businessType, int businessId);

    void deleteOcBusinessTagByUniqueKey(BusinessTagVO.BusinessTag businessTag);

    void addOcBusinessTag(OcBusinessTag ocBusinessTag);

    void deleteOcBusinessTagById(int id);

    int countOcTagHasUsed(int tagId);
}
