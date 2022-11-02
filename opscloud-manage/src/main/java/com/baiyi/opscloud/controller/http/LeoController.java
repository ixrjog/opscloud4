package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.leo.LeoTemplateParam;
import com.baiyi.opscloud.domain.vo.leo.LeoTemplateVO;
import com.baiyi.opscloud.facade.leo.LeoTemplateFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2022/11/1 18:04
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/leo")
@Api(tags = "Leo管理")
@RequiredArgsConstructor
public class LeoController {

    private final LeoTemplateFacade leoTemplateFacade;

    @ApiOperation(value = "分页查询模板列表")
    @PostMapping(value = "/template/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<LeoTemplateVO.Template>> queryLeoTemplatePage(@RequestBody @Valid LeoTemplateParam.TemplatePageQuery pageQuery) {
        return new HttpResult<>(leoTemplateFacade.queryLeoTemplatePage(pageQuery));
    }

    @ApiOperation(value = "新增模板")
    @PostMapping(value = "/template/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addLeoTemplate(@RequestBody @Valid LeoTemplateParam.Template template) {
        leoTemplateFacade.addLeoTemplate(template);
        return HttpResult.SUCCESS;
    }

}
