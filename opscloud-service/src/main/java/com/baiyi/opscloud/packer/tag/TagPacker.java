package com.baiyi.opscloud.packer.tag;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/19 2:41 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TagPacker {

    private final TagService tagService;

    private final BusinessTagService businessTagService;

    private List<Tag> queryBusinessTagByParam(TagParam.BusinessQuery queryParam) {
        return tagService.queryBusinessTagByParam(queryParam);
    }

    public void wrap(TagVO.ITags iTags) {
        TagParam.BusinessQuery queryParam = TagParam.BusinessQuery.builder()
                .businessType(iTags.getBusinessType())
                .businessId(iTags.getBusinessId())
                .build();
        List<Tag> tags = queryBusinessTagByParam(queryParam);
        iTags.setTags(wrapVOList(tags));
    }

    public List<TagVO.Tag> wrapVOList(List<Tag> data) {
        return data.stream().map(e -> {
            TagVO.Tag tag = BeanCopierUtil.copyProperties(e, TagVO.Tag.class);
            tag.setBusinessTypeEnum(BusinessTypeEnum.getByType(e.getBusinessType()));
            tag.setQuantityUsed(businessTagService.countByTagId(tag.getId()));
            return tag;
        }).collect(Collectors.toList());
    }

}
