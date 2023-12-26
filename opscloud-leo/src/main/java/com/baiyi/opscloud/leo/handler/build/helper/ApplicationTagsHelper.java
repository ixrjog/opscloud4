package com.baiyi.opscloud.leo.handler.build.helper;

import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTag;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/12/15 20:51
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ApplicationTagsHelper {

    private final BusinessTagService bizTagService;

    private final TagService tagService;

    public String getTagsStr(Integer applicationId){
        SimpleBusiness simpleBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .businessId(applicationId)
                .build();

        List<BusinessTag> bizTags = bizTagService.queryByBusiness(simpleBusiness);
        return CollectionUtils.isEmpty(bizTags) ? "未定义标签" : Joiner.on("、").join(
                bizTags.stream().map(t ->
                        tagService.getById(t.getTagId()).getTagKey()
                ).collect(Collectors.toList()));
    }

}