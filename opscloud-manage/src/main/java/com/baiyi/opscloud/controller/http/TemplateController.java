package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.template.BusinessTemplateParam;
import com.baiyi.opscloud.domain.param.template.MessageTemplateParam;
import com.baiyi.opscloud.domain.param.template.TemplateParam;
import com.baiyi.opscloud.domain.vo.template.BusinessTemplateVO;
import com.baiyi.opscloud.domain.vo.template.MessageTemplateVO;
import com.baiyi.opscloud.domain.vo.template.TemplateVO;
import com.baiyi.opscloud.facade.template.TemplateFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/12/6 10:55 AM
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/template")
@Api(tags = "模板管理")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateFacade templateFacade;

    @ApiOperation(value = "分页查询模板列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TemplateVO.Template>> queryTemplatePage(@RequestBody @Valid TemplateParam.TemplatePageQuery pageQuery) {
        return new HttpResult<>(templateFacade.queryTemplatePage(pageQuery));
    }

    @ApiOperation(value = "新增模板")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<TemplateVO.Template> addTemplate(@RequestBody @Valid TemplateParam.Template template) {
        return new HttpResult<>(templateFacade.addTemplate(template));
    }

    @ApiOperation(value = "更新模板")
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<TemplateVO.Template> updateTemplate(@RequestBody @Valid TemplateParam.Template template) {
        return new HttpResult<>(templateFacade.updateTemplate(template));
    }

    @ApiOperation(value = "删除指定的模板")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTemplateById(@RequestParam int id) {
        templateFacade.deleteTemplateById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "分页查询业务模板列表")
    @PostMapping(value = "/business/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<BusinessTemplateVO.BusinessTemplate>> queryBusinessTemplatePage(@RequestBody @Valid BusinessTemplateParam.BusinessTemplatePageQuery pageQuery) {
        return new HttpResult<>(templateFacade.queryBusinessTemplatePage(pageQuery));
    }

    @ApiOperation(value = "新增业务模板")
    @PostMapping(value = "/business/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BusinessTemplateVO.BusinessTemplate> addBusinessTemplate(@RequestBody @Valid BusinessTemplateParam.BusinessTemplate businessTemplate) {
        return new HttpResult<>(templateFacade.addBusinessTemplate(businessTemplate));
    }

    @ApiOperation(value = "更新业务模板")
    @PutMapping(value = "/business/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BusinessTemplateVO.BusinessTemplate> updateBusinessTemplate(@RequestBody @Valid BusinessTemplateParam.BusinessTemplate businessTemplate) {
        return new HttpResult<>(templateFacade.updateBusinessTemplate(businessTemplate));
    }

    @ApiOperation(value = "删除指定的业务模板")
    @DeleteMapping(value = "/business/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteBusinessTemplateById(@RequestParam int id) {
        templateFacade.deleteBusinessTemplateById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "业务模板创建资产")
    @PutMapping(value = "/business/asset/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BusinessTemplateVO.BusinessTemplate> createAssetByBusinessTemplateId(@RequestParam int id) {
        return new HttpResult<>(templateFacade.createAssetByBusinessTemplate(id));
    }

    @ApiOperation(value = "扫描业务模板与业务对象的关联关系")
    @PutMapping(value = "/business/scan", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> scanBusinessTemplateByInstance(@RequestParam @Valid String instanceUuid) {
        templateFacade.scanBusinessTemplateByInstanceUuid(instanceUuid);
        return HttpResult.SUCCESS;
    }

    // MessageTemplate

    @ApiOperation(value = "分页查询消息模板列表")
    @PostMapping(value = "/message/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<MessageTemplateVO.MessageTemplate>> queryMessageTemplatePage(@RequestBody @Valid MessageTemplateParam.MessageTemplatePageQuery pageQuery) {
        return new HttpResult<>(templateFacade.queryMessageTemplatePage(pageQuery));
    }

    @ApiOperation(value = "更新消息模板列表")
    @PutMapping(value = "/message/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateMessageTemplate(@RequestBody @Valid MessageTemplateParam.UpdateMessageTemplate messageTemplate) {
        templateFacade.updateMessageTemplate(messageTemplate);
        return HttpResult.SUCCESS;
    }

}
