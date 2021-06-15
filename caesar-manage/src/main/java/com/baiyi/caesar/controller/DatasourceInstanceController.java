package com.baiyi.caesar.controller;

import com.baiyi.caesar.common.HttpResult;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.datasource.DsAccountGroupParam;
import com.baiyi.caesar.domain.param.datasource.DsAccountParam;
import com.baiyi.caesar.domain.vo.datasource.DsAccountGroupVO;
import com.baiyi.caesar.domain.vo.datasource.DsAccountVO;
import com.baiyi.caesar.facade.datasource.DsInstanceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/6/11 10:10 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/datasource/instance")
@Api(tags = "数据源实例")
public class DatasourceInstanceController {

    @Resource
    private DsInstanceFacade dsInstanceFacade;

    @ApiOperation(value = "拉取数据源账户")
    @GetMapping(value = "/account/pull", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> pullAccount(@RequestParam @Valid int id) {
        dsInstanceFacade.pullAccount(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "分页查询数据源账户列表")
    @PostMapping(value = "/account/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DsAccountVO.Account>> queryAccountPage(@RequestBody @Valid DsAccountParam.AccountPageQuery pageQuery) {
        return new HttpResult<>(dsInstanceFacade.queryAccountPage(pageQuery));
    }

    @ApiOperation(value = "拉取数据源账户组")
    @GetMapping(value = "/account/group/pull", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> pullAccountGroup(@RequestParam @Valid int id) {
        dsInstanceFacade.pullAccountGroup(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "分页查询数据源账户组列表")
    @PostMapping(value = "/account/group/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DsAccountGroupVO.Group>> queryAccountGroupPage(@RequestBody @Valid DsAccountGroupParam.GroupPageQuery pageQuery) {
        return new HttpResult<>(dsInstanceFacade.queryAccountGroupPage(pageQuery));
    }
}
