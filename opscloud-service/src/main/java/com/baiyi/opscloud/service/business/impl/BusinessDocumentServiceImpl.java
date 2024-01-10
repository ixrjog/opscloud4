package com.baiyi.opscloud.service.business.impl;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessDocument;
import com.baiyi.opscloud.mapper.BusinessDocumentMapper;
import com.baiyi.opscloud.service.business.BusinessDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author baiyi
 * @Date 2022/5/15 19:05
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class BusinessDocumentServiceImpl implements BusinessDocumentService {

    private final BusinessDocumentMapper bizDocumentMapper;

    @Override
    public void add(BusinessDocument businessDocument) {
        bizDocumentMapper.insert(businessDocument);
    }

    @Override
    public void update(BusinessDocument businessDocument) {
        bizDocumentMapper.updateByPrimaryKey(businessDocument);
    }

    @Override
    public BusinessDocument getById(int id) {
        return bizDocumentMapper.selectByPrimaryKey(id);
    }

    @Override
    public BusinessDocument getByBusiness(BaseBusiness.IBusiness iBusiness) {
        Example example = new Example(BusinessDocument.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", iBusiness.getBusinessType()).andEqualTo("businessId", iBusiness.getBusinessId());
        return bizDocumentMapper.selectOneByExample(example);
    }

    @Override
    public void deleteByUniqueKey(Integer businessType, Integer businessId) {
        Example example = new Example(BusinessDocument.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessType);
        criteria.andEqualTo("businessId", businessId);
        bizDocumentMapper.deleteByExample(example);
    }

}