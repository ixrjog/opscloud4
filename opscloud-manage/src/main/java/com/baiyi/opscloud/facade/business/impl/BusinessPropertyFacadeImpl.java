package com.baiyi.opscloud.facade.business.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessProperty;
import com.baiyi.opscloud.domain.vo.business.BusinessPropertyVO;
import com.baiyi.opscloud.facade.business.BusinessPropertyFacade;
import com.baiyi.opscloud.service.business.BusinessPropertyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/19 4:40 下午
 * @Version 1.0
 */
@Service
public class BusinessPropertyFacadeImpl implements BusinessPropertyFacade {

    @Resource
    private BusinessPropertyService businessPropertyService;

    @Override
    public void add(BusinessPropertyVO.Property property) {
        BusinessProperty businessProperty = BeanCopierUtil.copyProperties(property, BusinessProperty.class);
        businessPropertyService.add(businessProperty);
    }

    @Override
    public void update(BusinessPropertyVO.Property property) {
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
