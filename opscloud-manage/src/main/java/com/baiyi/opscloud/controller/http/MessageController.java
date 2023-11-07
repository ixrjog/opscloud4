package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.datasource.message.LXHLMessageResponse;
import com.baiyi.opscloud.domain.param.message.MessageParam;
import com.baiyi.opscloud.facade.message.MessageFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author 修远
 * @Date 2022/9/8 9:18 PM
 * @Since 1.0
 */
@RestController
@RequestMapping("/api/message")
@Tag(name = "消息发送")
@RequiredArgsConstructor
public class MessageController {

    private final MessageFacade messageFacade;

    @Operation(summary = "发送消息")
    @PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LXHLMessageResponse.SendMessage> sendMessage(@Valid @RequestBody MessageParam.SendMessage param) {
        return new HttpResult<>(messageFacade.sendMessage(param));
    }

    @Operation(summary = "发送消息 toGrafana")
    @PostMapping(value = "/send/grafana", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LXHLMessageResponse.SendMessage> sendMessage4Grafana(@RequestParam String media,
                                                                           @RequestParam String mobiles,
                                                                           @RequestParam String platform,
                                                                           @RequestParam String platformToken,
                                                                           @RequestBody MessageParam.GrafanaMessage param) {
        return new HttpResult<>(messageFacade.sendMessage(media, mobiles, platform, platformToken, param));
    }

}