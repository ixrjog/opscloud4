package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.sys.EnvParam;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.facade.sys.EnvFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2021/5/25 4:32 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/sys/env")
@Tag(name = "环境管理")
@RequiredArgsConstructor
public class EnvController {

    private final EnvFacade envFacade;

    @Operation(summary = "分页查询环境列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<EnvVO.Env>> queryEnvPage(@RequestBody @Valid EnvParam.EnvPageQuery pageQuery) {
        return new HttpResult<>(envFacade.queryEnvPage(pageQuery));
    }

    @Operation(summary = "新增环境")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addEnv(@RequestBody @Valid EnvParam.Env env) {
        envFacade.addEnv(env);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新环境")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateEnv(@RequestBody @Valid EnvParam.Env env) {
        envFacade.updateEnv(env);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除指定的环境")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteEnvById(@RequestParam int id) {
        envFacade.deleteEnvById(id);
        return HttpResult.SUCCESS;
    }

}