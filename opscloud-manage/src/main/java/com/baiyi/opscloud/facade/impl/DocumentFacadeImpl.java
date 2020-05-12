package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.DocumentDecorator;
import com.baiyi.opscloud.domain.generator.opscloud.OcDocument;
import com.baiyi.opscloud.domain.vo.document.DocumentVO;
import com.baiyi.opscloud.facade.DocumentFacade;
import com.baiyi.opscloud.service.document.OcDocumentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/12 5:37 下午
 * @Version 1.0
 */
@Service
public class DocumentFacadeImpl implements DocumentFacade {

    @Resource
    private OcDocumentService ocDocumentService;

    @Resource
    private DocumentDecorator documentDecorator;

    @Override
    public DocumentVO.Doc queryDocByKey(String docKey) {
        OcDocument ocDocument = ocDocumentService.queryOcDocumentByKey(docKey);
        DocumentVO.Doc doc = BeanCopierUtils.copyProperties(ocDocument, DocumentVO.Doc.class);
        return documentDecorator.decorator(doc);
    }

}
