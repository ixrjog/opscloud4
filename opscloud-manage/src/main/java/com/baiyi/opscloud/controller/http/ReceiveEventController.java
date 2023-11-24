package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitLabNotifyParam;
import com.baiyi.opscloud.facade.datasource.instance.GitLabFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2021/10/28 4:17 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/receive/event")
@Tag(name = "接收信息")
@RequiredArgsConstructor
@Slf4j
public class ReceiveEventController {

    public static final String GITLAB_TOKEN = "X-Gitlab-Token";

    private final GitLabFacade gitLabFacade;

    @Operation(summary = "GitLab API v4 hooks")
    @PostMapping(value = "/gitlab/v4/system/hooks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> receiveGitLabV4SystemHooks(@RequestHeader(GITLAB_TOKEN) @NotNull(message = "Header `X-Gitlab-Token` is null") String gitLabToken, @RequestBody @Valid GitLabNotifyParam.SystemHook systemHook) {
        try {
            gitLabFacade.consumeEventV4(systemHook, gitLabToken);
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
        return HttpResult.SUCCESS;
    }

    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public static class InvalidTokenException extends RuntimeException {
        @Serial
        private static final long serialVersionUID = -7714152822790396676L;
    }

}