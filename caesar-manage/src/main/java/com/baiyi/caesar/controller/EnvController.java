package com.baiyi.caesar.controller;

import com.baiyi.caesar.common.HttpResult;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.sys.EnvParam;
import com.baiyi.caesar.facade.sys.EnvFacade;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/5/25 4:32 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/sys/env")
@Api(tags = "环境管理")
public class EnvController {

    @Resource
    private EnvFacade envFacade;

    @ApiOperation(value = "分页查询环境列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<EnvVO.Env>> queryEnvPage(@RequestBody @Valid EnvParam.EnvPageQuery pageQuery) {
        return new HttpResult<>(envFacade.queryEnvPage(pageQuery));
    }

    @ApiOperation(value = "新增环境")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addEnv(@RequestBody @Valid EnvVO.Env env) {
        envFacade.addEnv(env);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新环境")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateEnv(@RequestBody @Valid EnvVO.Env env) {
        envFacade.updateEnv(env);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的环境")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteEnvById(@RequestParam int id) {
        envFacade.deleteEnvById(id);
        return HttpResult.SUCCESS;
    }
}
