package com.baiyi.opscloud.facade.tag;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.domain.param.tag.BusinessTagParam;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import com.baiyi.opscloud.domain.vo.tag.TagVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/19 2:33 下午
 * @Version 1.0
 */
public interface SimpleTagFacade {

    List<Tag> queryBusinessTagByParam(TagParam.BusinessQuery queryParam);

    List<TagVO.Tag> queryTagByBusinessType(Integer businessType);

    DataTable<TagVO.Tag> queryTagPage(TagParam.TagPageQuery pageQuery);

    void addTag(TagVO.Tag tag);

    void updateTag(TagVO.Tag tag);

    void updateBusinessTags(BusinessTagParam.UpdateBusinessTags updateBusinessTags);
}
