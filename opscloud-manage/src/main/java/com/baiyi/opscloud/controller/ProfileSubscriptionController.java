package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.profile.ProfileSubscriptionParam;
import com.baiyi.opscloud.domain.vo.profile.ProfileSubscriptionVO;
import com.baiyi.opscloud.facade.ProfileSubscriptionFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/7/9 8:57 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/profile/subscription")
@Api(tags = "配置文件订阅服务")
public class ProfileSubscriptionController {

    @Resource
    private ProfileSubscriptionFacade profileSubscriptionFacade;

    @ApiOperation(value = "分页查询配置文件订阅配置列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ProfileSubscriptionVO.ProfileSubscription>> queryProfileSubscriptionPage(@RequestBody @Valid ProfileSubscriptionParam.PageQuery pageQuery) {
        return new HttpResult<>(profileSubscriptionFacade.queryProfileSubscriptionPage(pageQuery));
    }

    @ApiOperation(value = "新增订阅配置")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addProfileSubscription(@RequestBody @Valid ProfileSubscriptionVO.ProfileSubscription profileSubscription) {
        return new HttpResult<>(profileSubscriptionFacade.addProfileSubscription(profileSubscription));
    }

    @ApiOperation(value = "更新订阅配置")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateProfileSubscription(@RequestBody @Valid ProfileSubscriptionVO.ProfileSubscription profileSubscription) {
        return new HttpResult<>(profileSubscriptionFacade.updateProfileSubscription(profileSubscription));
    }

    @ApiOperation(value = "发布")
    @GetMapping(value = "/publish", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> publishProfile(@RequestParam @Valid int id) {
        profileSubscriptionFacade.publishProfile(id);
        return HttpResult.SUCCESS;
    }

}
