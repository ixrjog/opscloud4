package com.baiyi.caesar.service.tag.impl;

import com.baiyi.caesar.domain.generator.caesar.BusinessTag;
import com.baiyi.caesar.domain.generator.caesar.Tag;
import com.baiyi.caesar.service.tag.BaseTagService;
import com.baiyi.caesar.service.tag.BusinessTagService;
import com.baiyi.caesar.service.tag.TagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/23 10:37 上午
 * @Version 1.0
 */
@Service
public class BaseTagServiceImpl implements BaseTagService {

    @Resource
    private TagService tagService;

    @Resource
    private BusinessTagService businessTagService;

    // 检查业务是否有标签
    @Override
    public boolean hasBusinessTag(String tagKey, Integer businessType, Integer businessId, boolean isConstraint) {
        Tag tag = tagService.getByTagKey(tagKey);
        if (tag == null) return false;
        if (isConstraint && (!tag.getBusinessType().equals(businessType)))
            return false;
        BusinessTag businessTag = BusinessTag.builder()
                .businessId(businessId)
                .businessType(businessType)
                .tagId(tag.getId())
                .build();
        return businessTagService.countByParam(businessTag) > 0;
    }

}
