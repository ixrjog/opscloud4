package com.baiyi.caesar.facade.sys.impl;

import com.baiyi.caesar.common.exception.common.CommonRuntimeException;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.BusinessTag;
import com.baiyi.caesar.domain.generator.caesar.Tag;
import com.baiyi.caesar.domain.param.tag.BusinessTagParam;
import com.baiyi.caesar.domain.param.tag.TagParam;
import com.baiyi.caesar.facade.sys.TagFacade;
import com.baiyi.caesar.packer.tag.TagPacker;
import com.baiyi.caesar.service.tag.BusinessTagService;
import com.baiyi.caesar.service.tag.TagService;
import com.baiyi.caesar.domain.vo.tag.TagVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/19 2:33 下午
 * @Version 1.0
 */
@Service
public class TagFacadeImpl implements TagFacade {

    @Resource
    private TagService tagService;

    @Resource
    private BusinessTagService businessTagService;

    @Override
    public List<Tag> queryBusinessTagByParam(TagParam.BusinessQuery queryParam) {
        return tagService.queryBusinessTagByParam(queryParam);
    }

    @Override
    public List<TagVO.Tag> queryTagByBusinessType(Integer businessType) {
        List<Tag> tags = tagService.queryTagByBusinessType(businessType);
        return TagPacker.wrapVOList(tags);
    }

    @Override
    public DataTable<TagVO.Tag> queryTagPage(TagParam.TagPageQuery pageQuery) {
        DataTable<Tag> table = tagService.queryPageByParam(pageQuery);
        return new DataTable<>(TagPacker.wrapVOList(table.getData()), table.getTotalNum());
    }

    @Override
    public void addTag(TagVO.Tag tag) {
        try {
            tagService.add(BeanCopierUtil.copyProperties(tag, Tag.class));
        } catch (Exception ex) {
            throw new CommonRuntimeException(ErrorEnum.TAG_ADD_ERROR);
        }
    }

    @Override
    public void updateTag(TagVO.Tag tag) {
        Tag pre = tagService.getById(tag.getId());
        pre.setColor(tag.getColor());
        pre.setComment(tag.getComment());
        pre.setBusinessType(tag.getBusinessType());
        try {
            tagService.update(pre);
        } catch (Exception ex) {
            throw new CommonRuntimeException(ErrorEnum.TAG_UPDATE_ERROR);
        }
    }

    @Override
    public void updateBusinessTags(BusinessTagParam.UpdateBusinessTags updateBusinessTags) {
        List<BusinessTag> businessTags = businessTagService.queryByParam(updateBusinessTags);
        updateBusinessTags.getTagIds().forEach(tagId -> {
            if (checkAddBusinessTag(businessTags, tagId)) {
                BusinessTag businessTag = BusinessTag.builder()
                        .businessType(updateBusinessTags.getBusinessType())
                        .businessId(updateBusinessTags.getBusinessId())
                        .tagId(tagId)
                        .build();
                businessTagService.add(businessTag);
            }
        });
        businessTags.forEach(e -> businessTagService.deleteById(e.getId()));
    }

    private boolean checkAddBusinessTag(List<BusinessTag> businessTags, Integer tagId) {
        Iterator<BusinessTag> iter = businessTags.iterator();
        while (iter.hasNext()) {
            BusinessTag businessTag = iter.next();
            if (tagId.equals(businessTag.getTagId())) {
                iter.remove();
                return false;
            }
        }
        return true;
    }
}
