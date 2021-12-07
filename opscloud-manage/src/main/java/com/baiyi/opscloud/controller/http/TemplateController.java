package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.template.BusinessTemplateParam;
import com.baiyi.opscloud.domain.param.template.TemplateParam;
import com.baiyi.opscloud.domain.vo.template.BusinessTemplateVO;
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
@Api(tags = "模版管理")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateFacade templateFacade;

    @ApiOperation(value = "分页查询模版列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TemplateVO.Template>> queryTemplatePage(@RequestBody @Valid TemplateParam.TemplatePageQuery pageQuery) {
        return new HttpResult<>(templateFacade.queryTemplatePage(pageQuery));
    }

    @ApiOperation(value = "分页查询业务模版列表")
    @PostMapping(value = "/business/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<BusinessTemplateVO.BusinessTemplate>> queryBusinessTemplatePage(@RequestBody @Valid BusinessTemplateParam.BusinessTemplatePageQuery pageQuery) {
        return new HttpResult<>(templateFacade.queryBusinessTemplatePage(pageQuery));
    }

    @ApiOperation(value = "新增业务模版")
    @PostMapping(value = "/business/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BusinessTemplateVO.BusinessTemplate> addBusinessTemplate(@RequestBody @Valid BusinessTemplateParam.BusinessTemplate businessTemplate) {
        return new HttpResult<>(templateFacade.addBusinessTemplate(businessTemplate));
    }

    @ApiOperation(value = "更新业务模版")
    @PostMapping(value = "/business/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BusinessTemplateVO.BusinessTemplate> updateBusinessTemplate(@RequestBody @Valid BusinessTemplateParam.BusinessTemplate businessTemplate) {
        return new HttpResult<>(templateFacade.updateBusinessTemplate(businessTemplate));
    }

    @ApiOperation(value = "业务模版创建资产")
    @PutMapping(value = "/business/asset/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<BusinessTemplateVO.BusinessTemplate> createAssetByBusinessTemplateId(@RequestParam @Valid int id) {
        return new HttpResult<>(templateFacade.createAssetByBusinessTemplate(id));
    }

}
