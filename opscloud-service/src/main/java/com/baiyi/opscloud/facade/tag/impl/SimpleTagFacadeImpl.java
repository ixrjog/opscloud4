package com.baiyi.opscloud.facade.tag.impl;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTag;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.domain.param.tag.BusinessTagParam;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.facade.tag.SimpleTagFacade;
import com.baiyi.opscloud.facade.user.UserPermissionFacade;
import com.baiyi.opscloud.packer.TagPacker;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/19 2:33 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class SimpleTagFacadeImpl implements SimpleTagFacade {

    private final TagService tagService;

    private final BusinessTagService businessTagService;

    private final TagPacker tagPacker;

    private final UserPermissionFacade userPermissionFacade;

    @Override
    public List<TagVO.Tag> queryTagByBusiness(BaseBusiness.IBusiness iBusiness) {
        List<BusinessTag> businessTags = businessTagService.queryByBusiness(iBusiness);
        if (CollectionUtils.isEmpty(businessTags)) {
            return Collections.emptyList();
        }
        List<Tag> tags = businessTags.stream().map(e -> tagService.getById(e.getTagId())).collect(Collectors.toList());
        return BeanCopierUtil.copyListProperties(tags, TagVO.Tag.class).stream().peek(tagPacker::wrap).collect(Collectors.toList());
    }

    @Override
    public List<TagVO.Tag> queryTagByBusinessType(Integer businessType) {
        List<Tag> tags = tagService.queryTagByBusinessType(businessType);
        if (CollectionUtils.isEmpty(tags)) {
            return Collections.emptyList();
        }
        return BeanCopierUtil.copyListProperties(tags, TagVO.Tag.class).stream().peek(tagPacker::wrap).collect(Collectors.toList());
    }

    @Override
    public List<Tag> getFinOpsTags() {
        return tagService.queryFinOpsTags();
    }

    @Override
    public DataTable<TagVO.Tag> queryTagPage(TagParam.TagPageQuery pageQuery) {
        DataTable<Tag> table = tagService.queryPageByParam(pageQuery);
        List<TagVO.Tag> data = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(table.getData())) {
            data = BeanCopierUtil.copyListProperties(table.getData(), TagVO.Tag.class).stream().peek(tagPacker::wrap).collect(Collectors.toList());
        }
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void addTag(TagParam.Tag tag) {
        try {
            tagService.add(BeanCopierUtil.copyProperties(tag, Tag.class));
        } catch (Exception ex) {
            throw new OCException(ErrorEnum.TAG_ADD_ERROR);
        }
    }

    @Override
    public void updateTag(TagParam.Tag tag) {
        Tag saveTag = Tag.builder()
                .id(tag.getId())
                .color(tag.getColor())
                .comment(tag.getComment())
                .businessType(tag.getBusinessType())
                .build();
        try {
            tagService.updateByPrimaryKeySelective(saveTag);
        } catch (Exception ex) {
            throw new OCException(ErrorEnum.TAG_UPDATE_ERROR);
        }
    }

    @Override
    public void updateBusinessTags(BusinessTagParam.UpdateBusinessTags updateBusinessTags) {
        List<BusinessTag> businessTags = businessTagService.queryByBusiness(updateBusinessTags);
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
        businessTags.forEach(this::deleteBizTag);
    }

    /**
     * 增加业务逻辑 SA标签只有SUPER_ADMIN角色才能删除
     *
     * @param bizTag
     */
    private void deleteBizTag(BusinessTag bizTag) {
        Tag tag = tagService.getById(bizTag.getTagId());
        if (tag != null && TagConstants.SUPER_ADMIN.getTag().equals(tag.getTagKey())) {
            int accessLevel = userPermissionFacade.getUserAccessLevel(SessionHolder.getUsername());
            if (accessLevel < 100) {
                return;
            }
        }
        businessTagService.deleteById(bizTag.getId());
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

    @Override
    public void deleteTagById(int id) {
        if (businessTagService.countByTagId(id) > 0) {
            throw new OCException("标签使用中！");
        }
        tagService.deleteById(id);
    }

}