package com.baiyi.opscloud.decorator.document;

import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.common.util.TemplateUtils;
import com.baiyi.opscloud.domain.vo.document.DocumentVO;
import com.baiyi.opscloud.facade.DubboMappingFacade;
import com.baiyi.opscloud.jumpserver.config.JumpserverConfig;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/12 5:51 下午
 * @Version 1.0
 */
@Component
public class DocumentDecorator {

    @Resource
    private JumpserverConfig jumpserverConfig;

    @Resource
    private DubboMappingFacade dubboMappingFacade;

    public static final String DOC_KEY_DUBBO_RESOLVE_PREVIEW = "DUBBO_RESOLVE_PREVIEW";

    public DocumentVO.Doc decorator(DocumentVO.Doc doc) {

        Map<String, String> valuesMap = Maps.newHashMap();
        valuesMap.put("animal", "quick brown fox");
        valuesMap.put("username", SessionUtils.getUsername());
        valuesMap.put("cocoHost", jumpserverConfig.getCoco());
        if (doc.getDocKey().equals(DOC_KEY_DUBBO_RESOLVE_PREVIEW)) {
            valuesMap.put("dubbo-resolve-dev.properties", dubboMappingFacade.buildDubboResolveByEnv("dev"));
            valuesMap.put("dubbo-resolve-daily.properties", dubboMappingFacade.buildDubboResolveByEnv("daily"));
        }

        doc.setPreviewDoc(TemplateUtils.getTemplate(doc.getDocContent(), valuesMap));
        doc.setDocContent("");
        return doc;
    }


}
