package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.constants.enums.CredentialKindEnum;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.sys.CredentialParam;
import com.baiyi.opscloud.domain.vo.common.OptionsVO;
import com.baiyi.opscloud.domain.vo.sys.CredentialVO;
import com.baiyi.opscloud.facade.sys.CredentialFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2021/5/17 5:15 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/sys/credential")
@Tag(name = "凭据管理")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialFacade credentialFacade;

    @Operation(summary = "分页查询系统凭据列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CredentialVO.Credential>> queryCredentialPage(@RequestBody @Valid CredentialParam.CredentialPageQuery pageQuery) {
        return new HttpResult<>(credentialFacade.queryCredentialPage(pageQuery));
    }

    @Operation(summary = "查询系统凭据分类选项")
    @GetMapping(value = "/kind/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getCredentialKindOptions() {
        return new HttpResult<>(CredentialKindEnum.toOptions());
    }

    @Operation(summary = "新增系统凭据配置")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addCredential(@RequestBody @Valid CredentialParam.Credential credential) {
        credentialFacade.addCredential(credential);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新系统凭据配置")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateDsConfig(@RequestBody @Valid CredentialParam.Credential credential) {
        credentialFacade.updateCredential(credential);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除指定的系统凭据配置")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteCredentialById(@RequestParam @Valid int id) {
        credentialFacade.deleteCredentialById(id);
        return HttpResult.SUCCESS;
    }

}