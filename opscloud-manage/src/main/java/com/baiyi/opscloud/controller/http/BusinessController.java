package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.server.business.BusinessDocumentParam;
import com.baiyi.opscloud.domain.param.server.business.BusinessPropertyParam;
import com.baiyi.opscloud.domain.vo.business.BusinessDocumentVO;
import com.baiyi.opscloud.facade.business.BusinessDocumentFacade;
import com.baiyi.opscloud.facade.business.BusinessPropertyFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2021/8/20 2:07 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/business")
@Tag(name = "业务对象管理")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessPropertyFacade businessPropertyFacade;

    private final BusinessDocumentFacade businessDocumentFacade;

    @Operation(summary = "新增业务对象属性配置")
    @PostMapping(value = "/property/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addBusinessProperty(@RequestBody @Valid BusinessPropertyParam.Property property) {
        businessPropertyFacade.add(property);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新业务对象属性配置")
    @PutMapping(value = "/property/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateBusinessProperty(@RequestBody @Valid BusinessPropertyParam.Property property) {
        businessPropertyFacade.update(property);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "按ID查询业务文档")
    @GetMapping(value = "/document/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BusinessDocumentVO.Document> getBusinessDocumentById(@RequestParam @Valid Integer id) {
        return new HttpResult<>(businessDocumentFacade.getById(id));
    }

    @Operation(summary = "按联合键查询业务文档")
    @GetMapping(value = "/document/unique/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BusinessDocumentVO.Document> getBusinessDocumentByUniqueKey(@RequestParam @Valid Integer businessType, @Valid Integer businessId) {
        return new HttpResult<>(businessDocumentFacade.getByUniqueKey(businessType, businessId));
    }

    @Operation(summary = "保存业务文档")
    @PostMapping(value = "/document/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveBusinessDocument(@RequestBody @Valid BusinessDocumentParam.Document document) {
        businessDocumentFacade.save(document);
        return HttpResult.SUCCESS;
    }

}