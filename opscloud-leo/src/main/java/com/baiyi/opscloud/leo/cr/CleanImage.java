package com.baiyi.opscloud.leo.cr;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTag;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 09:51
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CleanImage {

    private final TagService tagService;

    private final BusinessTagService businessTagService;

    private final LeoJobService leoJobService;

    private static final String CLEAN_IMAGE_TAG = "CleanImage";

    private static final int DAILY_ENV_TYPE = 2;

    public void doTask() {
        Tag tag = tagService.getByTagKey(CLEAN_IMAGE_TAG);
        if (tag == null) return;
        List<BusinessTag> bizTags = businessTagService.queryByBusinessTag(BusinessTypeEnum.APPLICATION.getType(), tag.getId());
        if (CollectionUtils.isEmpty(bizTags)) return;
        bizTags.forEach(bizTag -> {
            int applicationId = bizTag.getBusinessId();
            List<LeoJob> leoJobs = leoJobService.queryJob(applicationId, DAILY_ENV_TYPE);
            if (CollectionUtils.isEmpty(leoJobs)) return;
        });
    }

}
