package com.baiyi.opscloud.facade.leo.tags;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.domain.param.tag.BusinessTagParam;
import com.baiyi.opscloud.facade.tag.SimpleTagFacade;
import com.baiyi.opscloud.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.glassfish.jersey.internal.guava.Sets;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2022/11/14 14:02
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeoTagHelper {

    private final TagService tagService;

    private final SimpleTagFacade simpleTagFacade;

    /**
     * Leo对象绑定标签
     *
     * @param leoBusiness
     * @param tags
     */
    public void updateTagsWithLeoBusiness(BaseBusiness.IBusiness leoBusiness, List<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return;
        }
        Set<Integer> tagIds = Sets.newHashSet();
        tags.stream().map(tagService::getByTagKey).filter(Objects::nonNull).map(Tag::getId).forEachOrdered(tagIds::add);
        BusinessTagParam.UpdateBusinessTags updateBusinessTags = BusinessTagParam.UpdateBusinessTags.builder()
                .businessId(leoBusiness.getBusinessId())
                .businessType(leoBusiness.getBusinessType())
                .tagIds(tagIds)
                .build();
        simpleTagFacade.updateBusinessTags(updateBusinessTags);
    }

}
