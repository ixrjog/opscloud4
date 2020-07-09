package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.profile.ProfileSubscriptionParam;
import com.baiyi.opscloud.domain.vo.profile.ProfileSubscriptionVO;
import com.baiyi.opscloud.facade.ProfileSubscriptionFacade;
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
    @GetMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ProfileSubscriptionVO.ProfileSubscription>> queryProfileSubscriptionPage(@Valid ProfileSubscriptionParam.PageQuery pageQuery) {
        return new HttpResult<>(profileSubscriptionFacade.queryProfileSubscriptionPage(pageQuery));
    }

}
