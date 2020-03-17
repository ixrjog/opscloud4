package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcBusinessTag;
import com.baiyi.opscloud.domain.generator.opscloud.OcTag;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import com.baiyi.opscloud.domain.vo.tag.OcBusinessTagVO;
import com.baiyi.opscloud.domain.vo.tag.OcTagVO;
import com.baiyi.opscloud.facade.TagFacade;
import com.baiyi.opscloud.service.tag.OcBusinessTagService;
import com.baiyi.opscloud.service.tag.OcTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/2/22 1:20 下午
 * @Version 1.0
 */
@Service
public class TagFacadeImpl implements TagFacade {

    @Resource
    private OcTagService ocTagService;

    @Resource
    private OcBusinessTagService ocBusinessTagService;

    public static final boolean ACTION_ADD = true;
    public static final boolean ACTION_UPDATE = false;

    @Override
    public DataTable<OcTagVO.Tag> queryTagPage(TagParam.PageQuery pageQuery) {
        DataTable<OcTag> table = ocTagService.queryOcTagByParam(pageQuery);
        List<OcTagVO.Tag> page = BeanCopierUtils.copyListProperties(table.getData(), OcTagVO.Tag.class);
        DataTable<OcTagVO.Tag> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> addTag(OcTagVO.Tag tag) {
        return saveTag(tag, ACTION_ADD);
    }

    @Override
    public BusinessWrapper<Boolean> updateTag(OcTagVO.Tag tag) {
        return saveTag(tag, ACTION_UPDATE);
    }

    private BusinessWrapper<Boolean> saveTag(OcTagVO.Tag tag, boolean action) {
        OcTag checkOcTagKey = ocTagService.queryOcTagByKey(tag.getTagKey());
        OcTag ocTag = BeanCopierUtils.copyProperties(tag, OcTag.class);
        // 对象存在 && 新增
        if (checkOcTagKey != null && action)
            return new BusinessWrapper<>(ErrorEnum.TAG_KEY_ALREADY_EXIST);
        if (action) {
            ocTagService.addOcTag(ocTag);
        } else {
            ocTagService.updateOcTag(ocTag);
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteTagById(int id) {
        OcTag ocTag = ocTagService.queryOcTagById(id);
        if (ocTag == null)
            return new BusinessWrapper<>(ErrorEnum.TAG_NOT_EXIST);
        // 判断server绑定的资源
        // int count = ocServerService.countByEnvType(ocEnv.getEnvType());
        int count = 0;
        if (count == 0) {
            ocTagService.deleteOcTagById(id);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.TAG_HAS_USED);
        }
    }

    @Override
    public List<OcTagVO.Tag> queryBusinessTag(TagParam.BusinessQuery businessQuery) {
        List<OcTag> ocTagList = ocTagService.queryOcTagByParam(businessQuery);
        return BeanCopierUtils.copyListProperties(ocTagList, OcTagVO.Tag.class);
    }

    @Override
    public List<OcTagVO.Tag> queryNotInBusinessTag(TagParam.BusinessQuery businessQuery) {
        List<OcTag> ocTagList = ocTagService.queryOcTagNotInByParam(businessQuery);
        return BeanCopierUtils.copyListProperties(ocTagList, OcTagVO.Tag.class);
    }

    @Transactional
    @Override
    public BusinessWrapper<Boolean> updateBusinessTag(OcBusinessTagVO.BusinessTag businessTag) {
        TagParam.BusinessQuery businessQuery = new TagParam.BusinessQuery();
        businessQuery.setBusinessType(businessTag.getBusinessType());
        businessQuery.setBusinessId(businessTag.getBusinessId());
        // 业务对象所有的标签
        List<OcTag> tagList = ocTagService.queryOcTagByParam(businessQuery);
        Map<Integer, OcTag> tagMap = getTagMap(tagList);
        for (Integer tagId : businessTag.getTagIds()) {
            OcBusinessTag ocBusinessTag = queryOcBusinessTag(businessTag, tagId);
            if (ocBusinessTag == null) {
                ocBusinessTagService.addOcBusinessTag(getOcBusinessTag(businessTag, tagId));
            } else {
                tagMap.remove(tagId);
            }
        }
        for (Integer tagId : tagMap.keySet()) {
            businessTag.setTagId(tagId);
            ocBusinessTagService.deleteOcBusinessTagByUniqueKey(businessTag);
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public List<OcBusinessTag> queryOcBusinessTagByBusinessTypeAndBusinessId(int businessType, int businessId) {
        return ocBusinessTagService.queryOcBusinessTagByBusinessTypeAndBusinessId(businessType, businessId);
    }

    @Override
    public void deleteTagByList(List<OcBusinessTag> ocBusinessTagList) {
        for (OcBusinessTag ocBusinessTag : ocBusinessTagList)
            deleteTagById(ocBusinessTag.getId());
    }

    private OcBusinessTag getOcBusinessTag(OcBusinessTagVO.BusinessTag businessTag, int tagId) {
        businessTag.setTagId(tagId);
        return BeanCopierUtils.copyProperties(businessTag, OcBusinessTag.class);
    }

    private OcBusinessTag queryOcBusinessTag(OcBusinessTagVO.BusinessTag businessTag, int tagId) {
        businessTag.setTagId(tagId);
        return ocBusinessTagService.queryOcBusinessTagByUniqueKey(businessTag);
    }

    private Map<Integer, OcTag> getTagMap(List<OcTag> tagList) {
        /**
         * List -> Map
         * 需要注意的是：
         * toMap 如果集合对象有重复的key，会报错Duplicate key ....
         * apple1,apple12的id都为1。
         * 可以用 (k1,k2)->k1 来设置，如果有重复的key,则保留key1,舍弃key2
         */
        Map<Integer, OcTag> tagMap = tagList.stream().collect(Collectors.toMap(OcTag::getId, a -> a, (k1, k2) -> k1));
        return tagMap;
    }

}
