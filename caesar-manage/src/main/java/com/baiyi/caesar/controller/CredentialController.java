package com.baiyi.caesar.controller;

import com.baiyi.caesar.common.HttpResult;
import com.baiyi.caesar.common.type.CredentialKindEnum;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.sys.CredentialParam;
import com.baiyi.caesar.domain.vo.common.OptionsVO;
import com.baiyi.caesar.domain.vo.sys.CredentialVO;
import com.baiyi.caesar.facade.sys.CredentialFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/5/17 5:15 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/sys/credential")
@Api(tags = "系统管理")
public class CredentialController {

    @Resource
    private CredentialFacade credentialFacade;

    @ApiOperation(value = "分页查询系统凭据列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CredentialVO.Credential>> queryCredentialPage(@RequestBody @Valid CredentialParam.CredentialPageQuery pageQuery) {
        return new HttpResult<>(credentialFacade.queryCredentialPage(pageQuery));
    }

    @ApiOperation(value = "查询系统凭据分类选项")
    @GetMapping(value = "/kind/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getCredentialKindOptions() {
        return new HttpResult<>(CredentialKindEnum.toOptions());
    }

    @ApiOperation(value = "新增系统凭据配置")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addCredential(@RequestBody @Valid CredentialVO.Credential credential) {
        credentialFacade.addCredential(credential);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新系统凭据配置")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateDsConfig(@RequestBody @Valid CredentialVO.Credential credential) {
        credentialFacade.updateCredential(credential);
        return HttpResult.SUCCESS;
    }

}
