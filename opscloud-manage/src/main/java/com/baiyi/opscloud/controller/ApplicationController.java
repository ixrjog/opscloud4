package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.facade.application.ApplicationFacade;
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
 * @Date 2021/7/13 3:37 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/application")
@Api(tags = "应用管理")
public class ApplicationController {

    @Resource
    private ApplicationFacade applicationFacade;

    @ApiOperation(value = "分页查询容器应用列表")
    @PostMapping(value = "/kubernetes/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ApplicationVO.Application>> queryApplicationKubernetesPage(@RequestBody @Valid ApplicationParam.ApplicationPageQuery pageQuery) {
        return new HttpResult<>(applicationFacade.queryApplicationPageByWebTerminal(pageQuery));
    }

}
