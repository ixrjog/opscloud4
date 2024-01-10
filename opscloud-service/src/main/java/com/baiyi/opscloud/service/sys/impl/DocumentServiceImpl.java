package com.baiyi.opscloud.service.sys.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Document;
import com.baiyi.opscloud.domain.param.sys.DocumentParam;
import com.baiyi.opscloud.mapper.DocumentMapper;
import com.baiyi.opscloud.service.sys.DocumentService;
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
 * @Date 2021/6/16 11:33 上午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentMapper documentMapper;

    @Override
    public Document getByKey(String key) {
        Example example = new Example(Document.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("documentKey", key);
        return documentMapper.selectOneByExample(example);
    }

    @Override
    public List<Document> queryByMountZone(String mountZone) {
        Example example = new Example(Document.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("mountZone", mountZone)
                .andEqualTo("isActive", true);
        example.setOrderByClause("seq");
        return documentMapper.selectByExample(example);
    }

    @Override
    public DataTable<Document> queryPageByParam(DocumentParam.DocumentPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(Document.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            criteria.andLike("name", SQLUtil.toLike(pageQuery.getQueryName()));
        }
        if (StringUtils.isNotBlank(pageQuery.getMountZone())) {
            criteria.andEqualTo("mountZone", pageQuery.getMountZone());
        }
        if (pageQuery.getIsActive() != null) {
            criteria.andEqualTo("isActive", pageQuery.getIsActive());
        }
        example.setOrderByClause("mount_zone, seq");
        List<Document> data = documentMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public void add(Document document) {
        documentMapper.insert(document);
    }

    @Override
    public void updateByPrimaryKeySelective(Document document) {
        documentMapper.updateByPrimaryKeySelective(document);
    }

    @Override
    public void deleteById(int id) {
        documentMapper.deleteByPrimaryKey(id);
    }

}