package com.baiyi.opscloud.facade.tag;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.base.BaseBusiness;
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

    List<TagVO.Tag> queryTagByBusiness(BaseBusiness.IBusiness iBusiness);

    List<TagVO.Tag> queryTagByBusinessType(Integer businessType);

    List<Tag> getFinOpsTags();

    DataTable<TagVO.Tag> queryTagPage(TagParam.TagPageQuery pageQuery);

    void addTag(TagParam.Tag tag);

    void updateTag(TagParam.Tag tag);

    void updateBusinessTags(BusinessTagParam.UpdateBusinessTags updateBusinessTags);

    void deleteTagById(int id);

}