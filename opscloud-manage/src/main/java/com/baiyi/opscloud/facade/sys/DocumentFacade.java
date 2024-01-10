package com.baiyi.opscloud.facade.sys;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.sys.DocumentParam;
import com.baiyi.opscloud.domain.vo.sys.DocumentVO;

/**
 * @Author baiyi
 * @Date 2021/6/16 11:29 上午
 * @Version 1.0
 */
public interface DocumentFacade {

    void updateDocument(DocumentParam.UpdateDocument updateDocument);

    void updateDocumentZone(DocumentParam.UpdateDocumentZone updateDocumentZone);

    void addDocument(DocumentParam.AddDocument addDocument);

    void deleteDocumentById(int id);

    DataTable<DocumentVO.Zone> queryDocumentZonePage(DocumentParam.DocumentZonePageQuery query);

    DataTable<DocumentVO.Document> queryDocumentPage(DocumentParam.DocumentPageQuery query);

    DocumentVO.Doc previewDocument(DocumentParam.DocumentQuery query);

    DocumentVO.DocZone getDocZone(DocumentParam.DocumentZoneQuery query);

}