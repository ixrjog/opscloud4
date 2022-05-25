package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.vo.business.BusinessDocumentVO;
import com.baiyi.opscloud.domain.vo.business.BusinessPropertyVO;
import com.baiyi.opscloud.facade.business.BusinessDocumentFacade;
import com.baiyi.opscloud.facade.business.BusinessPropertyFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/8/20 2:07 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/business")
@Api(tags = "业务对象管理")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessPropertyFacade businessPropertyFacade;

    private final BusinessDocumentFacade businessDocumentFacade;

    @ApiOperation(value = "新增业务对象属性配置")
    @PostMapping(value = "/property/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addBusinessProperty(@RequestBody @Valid BusinessPropertyVO.Property property) {
        businessPropertyFacade.add(property);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新业务对象属性配置")
    @PutMapping(value = "/property/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateBusinessProperty(@RequestBody @Valid BusinessPropertyVO.Property property) {
        businessPropertyFacade.update(property);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "按ID查询业务文档")
    @GetMapping(value = "/document/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BusinessDocumentVO.Document> getBusinessDocumentById(@RequestParam @Valid Integer id) {
        return new HttpResult<>(businessDocumentFacade.getById(id));
    }

    @ApiOperation(value = "按UniqueKey查询业务文档")
    @GetMapping(value = "/document/unique/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BusinessDocumentVO.Document> getBusinessDocumentByUniqueKey(@RequestParam @Valid Integer businessType, @Valid Integer businessId) {
        return new HttpResult<>(businessDocumentFacade.getByUniqueKey(businessType, businessId));
    }

    @ApiOperation(value = "保存业务文档")
    @PostMapping(value = "/document/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveBusinessDocument(@RequestBody @Valid BusinessDocumentVO.Document document) {
        businessDocumentFacade.save(document);
        return HttpResult.SUCCESS;
    }

}
