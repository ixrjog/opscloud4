package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.vo.document.DocumentVO;
import com.baiyi.opscloud.facade.DocumentFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/5/12 6:02 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/doc")
@Api(tags = "环境管理")
public class DocumentController {

    @Resource
    private DocumentFacade documentFacade;

    @ApiOperation(value = "查询帮助文档")
    @GetMapping(value = "/key/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DocumentVO.Doc> queryEnvPage(@Valid String key) {
        return new HttpResult<>(documentFacade.queryDocByKey(key));
    }
}
