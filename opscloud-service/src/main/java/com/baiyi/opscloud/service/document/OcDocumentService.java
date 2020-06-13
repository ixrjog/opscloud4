package com.baiyi.opscloud.service.document;

import com.baiyi.opscloud.domain.generator.opscloud.OcDocument;

/**
 * @Author baiyi
 * @Date 2020/5/12 5:38 下午
 * @Version 1.0
 */
public interface OcDocumentService {

    OcDocument queryOcDocumentByKey(String docKey);

    OcDocument queryOcDocumentById(int id);
}
