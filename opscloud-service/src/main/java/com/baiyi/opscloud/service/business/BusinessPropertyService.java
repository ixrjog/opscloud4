package com.baiyi.opscloud.service.business;

import com.baiyi.opscloud.domain.generator.opscloud.BusinessProperty;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/21 2:40 下午
 * @Since 1.0
 */
public interface BusinessPropertyService {

    BusinessProperty getById(int id);

    void add(BusinessProperty businessProperty);

    void update(BusinessProperty businessProperty);

    void deleteById(int id);

    List<BusinessProperty> listByBusiness(Integer businessType, Integer businessId);
}
