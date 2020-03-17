package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcBusinessTag;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import com.baiyi.opscloud.domain.vo.tag.OcBusinessTagVO;
import com.baiyi.opscloud.domain.vo.tag.OcTagVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/22 1:17 下午
 * @Version 1.0
 */
public interface TagFacade {

    DataTable<OcTagVO.Tag> queryTagPage(TagParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addTag(OcTagVO.Tag tag);

    BusinessWrapper<Boolean> updateTag(OcTagVO.Tag tag);

    BusinessWrapper<Boolean> deleteTagById(int id);

    List<OcTagVO.Tag> queryBusinessTag(TagParam.BusinessQuery businessQuery);

    List<OcTagVO.Tag> queryNotInBusinessTag(TagParam.BusinessQuery businessQuery);

    BusinessWrapper<Boolean> updateBusinessTag(OcBusinessTagVO.BusinessTag businessTag);

    List<OcBusinessTag> queryOcBusinessTagByBusinessTypeAndBusinessId(int businessType, int businessId);

    void deleteTagByList(List<OcBusinessTag> ocBusinessTagList);

}
