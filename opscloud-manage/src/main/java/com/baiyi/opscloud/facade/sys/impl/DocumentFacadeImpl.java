package com.baiyi.opscloud.facade.sys.impl;

import com.baiyi.opscloud.common.builder.SimpleDict;
import com.baiyi.opscloud.common.builder.SimpleDictBuilder;
import com.baiyi.opscloud.common.config.CachingConfiguration;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.common.util.TemplateUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Document;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.sys.DocumentParam;
import com.baiyi.opscloud.domain.vo.sys.DocumentVO;
import com.baiyi.opscloud.facade.sys.DocumentFacade;
import com.baiyi.opscloud.service.sys.DocumentService;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    @Override
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_10MINUTES, key = "'preview_document_key_'+ #query.documentKey", unless = "#result == null")
    public DocumentVO.Doc previewDocument(DocumentParam.DocumentQuery query) {
        Document doc = documentService.getByKey(query.getDocumentKey());
        render(doc, query);
        return DocumentVO.Doc.builder()
                .content(doc.getContent())
                .dict(query.getDict())
                .build();
    }

    private void render(Document doc, DocumentParam.DocumentQuery query) {
        User user = userService.getByUsername(SessionUtil.getUsername());
        SimpleDict simpleDict = SimpleDictBuilder.newBuilder()
                .putParam("username", SessionUtil.getUsername())
                .putParam("email", user.getEmail())
                // 反向注入
                .putWithDict(query.getDict())
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
