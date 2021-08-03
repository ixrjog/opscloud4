package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.sys.DocumentParam;
import com.baiyi.opscloud.domain.vo.sys.DocumentVO;
import com.baiyi.opscloud.facade.sys.DocumentFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/6/16 11:23 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/sys/doc")
@Api(tags = "文档管理")
public class DocumentController {

    @Resource
    private DocumentFacade documentFacade;

    @ApiOperation(value = "查阅文档")
    @PostMapping(value = "/preview", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DocumentVO.Doc> previewDocument(@RequestBody @Valid DocumentParam.DocumentQuery query) {
        return new HttpResult<>(documentFacade.previewDocument(query));
    }

}
