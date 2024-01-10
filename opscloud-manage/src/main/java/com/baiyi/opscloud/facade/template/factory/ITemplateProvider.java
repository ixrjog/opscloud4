package com.baiyi.opscloud.facade.template.factory;

import com.baiyi.opscloud.domain.base.IInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTemplate;
import com.baiyi.opscloud.domain.vo.template.BusinessTemplateVO;

/**
 * @Author baiyi
 * @Date 2021/12/7 4:11 PM
 * @Version 1.0
 */
public interface ITemplateProvider extends IInstanceAsset {

    String getTemplateKey();

    BusinessTemplateVO.BusinessTemplate produce(BusinessTemplate bizTemplate);

}