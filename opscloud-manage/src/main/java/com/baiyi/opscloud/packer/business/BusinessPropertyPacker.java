package com.baiyi.opscloud.packer.business;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessProperty;
import com.baiyi.opscloud.domain.vo.business.BusinessPropertyVO;
import com.baiyi.opscloud.service.business.BusinessPropertyService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/21 5:13 下午
 * @Since 1.0
 */

@Component
public class BusinessPropertyPacker {

    @Resource
    private BusinessPropertyService businessPropertyService;

    public void wrap(BusinessPropertyVO.IBusinessProperty iBusinessProperty) {
        BusinessProperty businessProperty = businessPropertyService.getByUniqueKey(iBusinessProperty);
        if (businessProperty == null) return;
        iBusinessProperty.setBusinessProperty(BeanCopierUtil.copyProperties(businessProperty, BusinessPropertyVO.Property.class));
    }

}
