package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.cloud.AliyunRAMPolicyParam;
import com.baiyi.opscloud.domain.param.cloud.AliyunRAMUserParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunRAMVO;
import com.baiyi.opscloud.facade.AliyunRAMFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/6/9 5:25 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/aliyun/ram")
@Api(tags = "阿里云访问控制管理")
public class AliyunRAMController {

    @Resource
    private AliyunRAMFacade aliyunRAMFacade;

    @ApiOperation(value = "查询访问控制用户信息")
    @PostMapping(value = "/user/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AliyunRAMVO.RAMUser>> queryRAMUserPage(@RequestBody @Valid AliyunRAMUserParam.RamUserPageQuery pageQuery) {
        return new HttpResult<>(aliyunRAMFacade.queryRAMUserPage(pageQuery));
    }

    @ApiOperation(value = "同步用户")
    @GetMapping(value = "/user/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncUser() {
        return new HttpResult<>(aliyunRAMFacade.syncRAMUser());
    }

    @ApiOperation(value = "同步策略")
    @GetMapping(value = "/policy/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncPolicy() {
        return new HttpResult<>(aliyunRAMFacade.syncRAMPolicy());
    }

    @ApiOperation(value = "设置工单申请")
    @PutMapping(value = "/policy/workorder/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setPolicyWorkorder(@Valid int id) {
        return new HttpResult<>(aliyunRAMFacade.setRAMPolicyWorkorderById(id));
    }

    @ApiOperation(value = "查询访问控制策略信息")
    @PostMapping(value = "/policy/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AliyunRAMVO.RAMPolicy>> queryPolicyPage(@RequestBody @Valid AliyunRAMPolicyParam.PageQuery pageQuery) {
        return new HttpResult<>(aliyunRAMFacade.queryRAMPolicyPage(pageQuery));
    }

}
