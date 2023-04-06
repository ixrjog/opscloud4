package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitLabNotifyParam;
import com.baiyi.opscloud.facade.datasource.instance.GitlabFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author baiyi
 * @Date 2021/10/28 4:17 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/receive/event")
@Tag(name = "接收信息")
@RequiredArgsConstructor
public class ReceiveEventController {

    private final GitlabFacade gitlabFacade;

    @Operation(summary = "GitLab API v4 hooks接口")
    @PostMapping(value = "/gitlab/v4/system/hooks",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> receiveGitlabV4SystemHooks(@RequestBody @Valid GitLabNotifyParam.SystemHook systemHook) {
        gitlabFacade.consumeEventV4(systemHook);
        return HttpResult.SUCCESS;
    }

}
