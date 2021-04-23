package com.baiyi.opscloud.service.document.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcDocument;
import com.baiyi.opscloud.mapper.opscloud.OcDocumentMapper;
import com.baiyi.opscloud.service.document.OcDocumentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/12 5:38 下午
 * @Version 1.0
 */
@Service
public class OcDocumentServiceImpl implements OcDocumentService {

    @Resource
    private OcDocumentMapper ocDocumentMapper;

    @Override
    public OcDocument queryOcDocumentByKey(String docKey) {
        Example example = new Example(OcDocument.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("docKey", docKey);
        Page page = PageHelper.startPage(1,1);
        return ocDocumentMapper.selectOneByExample(example);
    }

    @Override
    public OcDocument queryOcDocumentById(int id) {
        return ocDocumentMapper.selectByPrimaryKey(id);
    }
}
