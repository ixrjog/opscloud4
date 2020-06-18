package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.vo.document.DocumentVO;
import com.baiyi.opscloud.facade.DocumentFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/5/12 6:02 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/doc")
@Api(tags = "文档管理")
public class DocumentController {

    @Resource
    private DocumentFacade documentFacade;

    @ApiOperation(value = "查询帮助文档")
    @GetMapping(value = "/key/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DocumentVO.Doc> queryDocByKey(@Valid String key) {
        return new HttpResult<>(documentFacade.queryDocByKey(key));
    }

    @ApiOperation(value = "查询帮助文档")
    @GetMapping(value = "/id/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DocumentVO.Doc> queryDocById(@Valid int id) {
        return new HttpResult<>(documentFacade.queryDocById(id));
    }

    @ApiOperation(value = "查询帮助文档")
    @GetMapping(value = "/user/type/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DocumentVO.UserDoc> queryUserDocByType(@Valid int docType) {
        return new HttpResult<>(documentFacade.queryUserDocByType(docType));
    }

    @ApiOperation(value = "更新帮助文档")
    @PutMapping(value = "/user/save",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveUserDoc(@RequestBody @Valid DocumentVO.UserDoc userDoc) {
        return new HttpResult<>(documentFacade.saveUserDoc(userDoc));
    }
}
