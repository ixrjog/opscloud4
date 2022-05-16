package com.baiyi.opscloud.facade.business.impl;

import com.baiyi.opscloud.common.annotation.BizDocWrapper;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.ansible.ServerGroupingAlgorithm;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessDocument;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.vo.business.BusinessDocumentVO;
import com.baiyi.opscloud.facade.business.BusinessDocumentFacade;
import com.baiyi.opscloud.service.business.BusinessDocumentService;
import com.baiyi.opscloud.service.server.ServerService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/5/16 11:34
 * @Version 1.0
 */
@Component
public class BusinessDocumentFacadeImpl implements BusinessDocumentFacade {

    @Resource
    private BusinessDocumentService businessDocumentService;

    @Resource
    private ServerGroupingAlgorithm serverGroupingAlgorithm;

    @Resource
    private ServerService serverService;

    @Override
    @BizDocWrapper(extend = true, wrapResult = true)
    public BusinessDocumentVO.Document getById(int id) {
        BusinessDocument businessDocument = businessDocumentService.getById(id);
        return BeanCopierUtil.copyProperties(businessDocument, BusinessDocumentVO.Document.class);
    }

    @Override
    public void add(BusinessDocumentVO.Document document) {
        BusinessDocument businessDocument = BeanCopierUtil.copyProperties(document, BusinessDocument.class);
        if (businessDocumentService.getByBusiness(document) != null) {
            throw new CommonRuntimeException("业务文档已存在!");
        }
        businessDocumentService.add(businessDocument);
        evictCache(document);
    }

    @Override
    public void update(BusinessDocumentVO.Document document) {
        BusinessDocument businessDocument = businessDocumentService.getByBusiness(document);
        businessDocument.setContent(document.getContent());
        businessDocumentService.update(businessDocument);
        evictCache(document);
    }

    private void evictCache(BaseBusiness.IBusiness iBusiness) {
        if (BusinessTypeEnum.SERVERGROUP.getType() == iBusiness.getBusinessType()) {
            serverGroupingAlgorithm.evictGrouping(iBusiness.getBusinessId());
            return;
        }
        if (BusinessTypeEnum.SERVER.getType() == iBusiness.getBusinessType()) {
            Server server = serverService.getById(iBusiness.getBusinessId());
            serverGroupingAlgorithm.evictGrouping(server.getServerGroupId());
        }
    }

}
