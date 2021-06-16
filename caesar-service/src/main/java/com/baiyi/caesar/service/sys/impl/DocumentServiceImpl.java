package com.baiyi.caesar.service.sys.impl;

import com.baiyi.caesar.domain.generator.caesar.Document;
import com.baiyi.caesar.mapper.caesar.DocumentMapper;
import com.baiyi.caesar.service.sys.DocumentService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/16 11:33 上午
 * @Version 1.0
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    @Resource
    private DocumentMapper documentMapper;

    @Override
    public Document getByKey(String key) {
        Example example = new Example(Document.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("documentKey", key);
        return documentMapper.selectOneByExample(example);
    }
}
