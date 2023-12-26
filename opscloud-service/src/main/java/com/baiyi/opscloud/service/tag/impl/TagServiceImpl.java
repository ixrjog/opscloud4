package com.baiyi.opscloud.service.tag.impl;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import com.baiyi.opscloud.mapper.TagMapper;
import com.baiyi.opscloud.service.tag.TagService;
import com.baiyi.opscloud.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/19 2:27 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    @Override
    public Tag getById(Integer id) {
        return tagMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Tag> queryBusinessTagByParam(TagParam.BusinessQuery queryParam) {
        return tagMapper.queryBusinessTagByParam(queryParam);
    }

    @Override
    public List<Tag> queryTagByBusinessType(Integer businessType) {
        Example example = new Example(Tag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessType)
                .orEqualTo("businessType", 0);
        return tagMapper.selectByExample(example);
    }

    @Override
    public List<Tag> queryFinOpsTags() {
        Example example = new Example(Tag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("tagKey", "$%");
        return tagMapper.selectByExample(example);
    }

    @Override
    public void add(Tag tag) {
        tagMapper.insert(tag);
    }

    @Override
    public void updateByPrimaryKeySelective(Tag tag) {
        tagMapper.updateByPrimaryKeySelective(tag);
    }

    @Override
    public void deleteById(Integer id) {
        tagMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Tag getByTagKey(String tagKey) {
        Example example = new Example(Tag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tagKey", tagKey);
        return tagMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<Tag> queryPageByParam(TagParam.TagPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(Tag.class);
        Example.Criteria criteria = example.createCriteria();
        if (IdUtil.isNotEmpty(pageQuery.getBusinessType())) {
            criteria.andEqualTo("businessType", pageQuery.getBusinessType());
            if (pageQuery.getAppend() != null && pageQuery.getAppend()) {
                criteria.orEqualTo("businessType", BusinessTypeEnum.COMMON.getType());
            }
        }
        if (StringUtils.isNotBlank(pageQuery.getTagKey())) {
            criteria.andLike("tagKey", SQLUtil.toLike(pageQuery.getTagKey()));
        }
        example.setOrderByClause("create_time");
        List<Tag> data = tagMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

}