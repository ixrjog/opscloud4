package com.baiyi.opscloud.packer.business;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessProperty;
import com.baiyi.opscloud.domain.vo.business.BusinessPropertyVO;
import com.baiyi.opscloud.service.business.BusinessPropertyService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/21 5:13 下午
 * @Since 1.0
 */

@Component
public class BusinessPropertyPacker {

    @Resource
    private BusinessPropertyService businessPropertyService;

    public void wrap(BusinessPropertyVO.IProperty iProperty) {
        List<BusinessProperty> properties = businessPropertyService.queryByBusiness(iProperty.getBusinessType(), iProperty.getBusinessType());
        iProperty.setBusinessProperties(wrapVOList(properties));
    }

    public List<BusinessPropertyVO.Property> wrapVOList(List<BusinessProperty> properties) {
        return BeanCopierUtil.copyListProperties(properties, BusinessPropertyVO.Property.class);
    }


}
