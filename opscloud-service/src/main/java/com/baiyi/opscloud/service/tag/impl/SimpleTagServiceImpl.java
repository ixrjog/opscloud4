package com.baiyi.opscloud.service.tag.impl;

import com.baiyi.opscloud.domain.generator.opscloud.BusinessTag;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.service.tag.SimpleTagService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author baiyi
 * @Date 2021/6/23 10:37 上午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class SimpleTagServiceImpl implements SimpleTagService {

    private final TagService tagService;

    private final BusinessTagService businessTagService;

    @Override
    public boolean hasBusinessTag(String tagKey, Integer businessType, Integer businessId, boolean isConstraint) {
        Tag tag = tagService.getByTagKey(tagKey);
        if (tag == null) {
            return false;
        }
        if (isConstraint && (!tag.getBusinessType().equals(businessType))) {
            return false;
        }
        BusinessTag businessTag = BusinessTag.builder()
                .businessId(businessId)
                .businessType(businessType)
                .tagId(tag.getId())
                .build();
        return businessTagService.countByBusinessTag(businessTag) > 0;
    }

    public void labeling(String tagKey, Integer businessType, Integer businessId) {
        Tag tag = tagService.getByTagKey(tagKey);
        if (tag == null) {
            return;
        }
        BusinessTag businessTag = BusinessTag.builder()
                .businessId(businessId)
                .businessType(businessType)
                .tagId(tag.getId())
                .build();
        if (businessTagService.countByBusinessTag(businessTag) == 0) {
            businessTagService.add(businessTag);
        }

    }

}