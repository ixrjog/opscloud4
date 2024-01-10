package com.baiyi.opscloud.service.tag;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.domain.param.tag.TagParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/19 2:26 下午
 * @Version 1.0
 */
public interface TagService {

    List<Tag> queryBusinessTagByParam(TagParam.BusinessQuery queryParam);

    List<Tag> queryTagByBusinessType(Integer businessType);

    List<Tag> queryFinOpsTags();

    Tag getById(Integer id);

    void add(Tag tag);

    void updateByPrimaryKeySelective(Tag tag);

    void deleteById(Integer id);

    Tag getByTagKey(String tagKey);

    DataTable<Tag> queryPageByParam(TagParam.TagPageQuery pageQuery);

}