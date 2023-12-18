package com.baiyi.opscloud.facade.business.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessProperty;
import com.baiyi.opscloud.domain.param.server.business.BusinessPropertyParam;
import com.baiyi.opscloud.facade.business.BusinessPropertyFacade;
import com.baiyi.opscloud.service.business.BusinessPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author baiyi
 * @Date 2021/8/19 4:40 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class BusinessPropertyFacadeImpl implements BusinessPropertyFacade {

    private final BusinessPropertyService businessPropertyService;

    @Override
    public void add(BusinessPropertyParam.Property property) {
        BusinessProperty businessProperty = BeanCopierUtil.copyProperties(property, BusinessProperty.class);
        businessPropertyService.add(businessProperty);
    }

    @Override
    public void update(BusinessPropertyParam.Property property) {
        BusinessProperty businessProperty = businessPropertyService.getByUniqueKey(property);
        if (businessProperty == null) {
            property.setId(null);
            add(property);
        } else {
            // 判断属性是否变更
            if (!AssetUtil.equals(businessProperty.getProperty(), property.getProperty())) {
                BusinessProperty pre = BeanCopierUtil.copyProperties(property, BusinessProperty.class);
                businessPropertyService.update(pre);
            }
        }
    }

}
