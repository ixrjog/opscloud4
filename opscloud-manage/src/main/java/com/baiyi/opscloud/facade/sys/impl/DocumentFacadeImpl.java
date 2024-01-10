package com.baiyi.opscloud.facade.sys.impl;

import com.baiyi.opscloud.common.builder.SimpleDict;
import com.baiyi.opscloud.common.builder.SimpleDictBuilder;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.common.util.TemplateUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Document;
import com.baiyi.opscloud.domain.generator.opscloud.DocumentZone;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.sys.DocumentParam;
import com.baiyi.opscloud.domain.vo.sys.DocumentVO;
import com.baiyi.opscloud.facade.sys.DocumentFacade;
import com.baiyi.opscloud.service.sys.DocumentService;
import com.baiyi.opscloud.service.sys.DocumentZoneService;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/16 11:29 上午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class DocumentFacadeImpl implements DocumentFacade {

    private final DocumentService documentService;

    private final UserService userService;

    private final DocumentZoneService documentZoneService;

    @Override
    public DataTable<DocumentVO.Document> queryDocumentPage(DocumentParam.DocumentPageQuery query) {
        DataTable<Document> table = documentService.queryPageByParam(query);
        List<DocumentVO.Document> data = BeanCopierUtil.copyListProperties(table.getData(), DocumentVO.Document.class);
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void addDocument(DocumentParam.AddDocument addDocument) {
        documentService.add(BeanCopierUtil.copyProperties(addDocument, Document.class));
    }

    @Override
    public void deleteDocumentById(int id) {
        documentService.deleteById(id);
    }

    @Override
    public void updateDocument(DocumentParam.UpdateDocument updateDocument) {
        documentService.updateByPrimaryKeySelective(BeanCopierUtil.copyProperties(updateDocument, Document.class));
    }

    @Override
    public void updateDocumentZone(DocumentParam.UpdateDocumentZone updateDocumentZone) {
        documentZoneService.updateByPrimaryKeySelective(BeanCopierUtil.copyProperties(updateDocumentZone, DocumentZone.class));
    }

    @Override
    public DataTable<DocumentVO.Zone> queryDocumentZonePage(DocumentParam.DocumentZonePageQuery query) {
        DataTable<DocumentZone> table = documentZoneService.queryPageByParam(query);
        List<DocumentVO.Zone> data = BeanCopierUtil.copyListProperties(table.getData(), DocumentVO.Zone.class);
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public DocumentVO.DocZone getDocZone(DocumentParam.DocumentZoneQuery query) {
        DocumentZone documentZone = documentZoneService.getByMountZone(query.getMountZone());
        if (documentZone == null) {
            return DocumentVO.DocZone.EMPTY;
        }
        List<Document> documents = documentService.queryByMountZone(query.getMountZone());
        if (CollectionUtils.isEmpty(documents)) {
            // 无有效文档
            return DocumentVO.DocZone.EMPTY;
        }
        User user = userService.getByUsername(SessionHolder.getUsername());
        return DocumentVO.DocZone.builder()
                .zone(BeanCopierUtil.copyProperties(documentZone, DocumentVO.Zone.class))
                .docs(documents.stream().map(doc -> toDoc(doc, user, query.getDict())).collect(Collectors.toList()))
                .build();
    }

    private DocumentVO.Doc toDoc(Document document, User user, Map<String, String> dict) {
        SimpleDict simpleDict = SimpleDictBuilder.newBuilder()
                .put("username", SessionHolder.getUsername())
                .put("email", user.getEmail())
                .put(dict)
                .build();
        String content = TemplateUtil.render(document.getContent(), simpleDict.getDict());
        return DocumentVO.Doc.builder()
                .name(document.getName())
                .icon(document.getIcon())
                .documentKey(document.getDocumentKey())
                .content(content)
                .dict(simpleDict.getDict())
                .build();
    }

    @Override
    // @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_10M, key = "'preview_document_key_'+ #query.documentKey", unless = "#result == null")
    public DocumentVO.Doc previewDocument(DocumentParam.DocumentQuery query) {
        Document doc = documentService.getByKey(query.getDocumentKey());
        render(doc, query);
        return DocumentVO.Doc.builder()
                .content(doc.getContent())
                .dict(query.getDict())
                .build();
    }

    private void render(Document doc, DocumentParam.DocumentQuery query) {
        User user = userService.getByUsername(SessionHolder.getUsername());
        SimpleDict simpleDict = SimpleDictBuilder.newBuilder()
                .put("username", SessionHolder.getUsername())
                .put("email", user.getEmail())
                // 反向注入
                .put(query.getDict())
                .build();
        renderWithDict(doc, simpleDict.getDict());
    }

    private void renderWithDict(Document doc, Map<String, String> dict) {
        if (dict != null) {
            String content = TemplateUtil.render(doc.getContent(), dict);
            doc.setContent(content);
        }
    }

}