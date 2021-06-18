package com.baiyi.caesar.packer.tag;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.Tag;
import com.baiyi.caesar.domain.param.tag.TagParam;
import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import com.baiyi.caesar.facade.sys.TagFacade;
import com.baiyi.caesar.domain.vo.tag.TagVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/19 2:41 下午
 * @Version 1.0
 */
@Component
public class TagPacker {

    @Resource
    private TagFacade tagFacade;

    public void wrap(TagVO.ITags iTags) {
        TagParam.BusinessQuery queryParam = TagParam.BusinessQuery.builder()
                .businessType(iTags.getBusinessType())
                .businessId(iTags.getBusinessId())
                .build();
        List<Tag> tags = tagFacade.queryBusinessTagByParam(queryParam);
        iTags.setTags(wrapVOList(tags));
    }

    public static List<TagVO.Tag> wrapVOList(List<Tag> data) {
        return data.stream().map(e -> {
            TagVO.Tag tag = BeanCopierUtil.copyProperties(e, TagVO.Tag.class);
            tag.setBusinessTypeEnum(BusinessTypeEnum.getByType(e.getBusinessType()));
            return tag;
        }).collect(Collectors.toList());
    }
}
