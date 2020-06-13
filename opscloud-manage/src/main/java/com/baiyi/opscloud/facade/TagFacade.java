package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcBusinessTag;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import com.baiyi.opscloud.domain.vo.tag.BusinessTagVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/22 1:17 下午
 * @Version 1.0
 */
public interface TagFacade {

    DataTable<TagVO.Tag> queryTagPage(TagParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addTag(TagVO.Tag tag);

    BusinessWrapper<Boolean> updateTag(TagVO.Tag tag);

    BusinessWrapper<Boolean> deleteTagById(int id);

    List<TagVO.Tag> queryBusinessTag(TagParam.BusinessQuery businessQuery);

    List<TagVO.Tag> queryNotInBusinessTag(TagParam.BusinessQuery businessQuery);

    BusinessWrapper<Boolean> updateBusinessTag(BusinessTagVO.BusinessTag businessTag);

    List<OcBusinessTag> queryOcBusinessTagByBusinessTypeAndBusinessId(int businessType, int businessId);

    void deleteTagByList(List<OcBusinessTag> ocBusinessTagList);

}
