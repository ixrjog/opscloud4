package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.datasource.message.LXHLMessageResponse;
import com.baiyi.opscloud.domain.param.message.MessageParam;
import com.baiyi.opscloud.facade.message.MessageFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 修远
 * @Date 2022/9/8 9:18 PM
 * @Since 1.0
 */
@RestController
@RequestMapping("/api/message")
@Api(tags = "消息发送")
@RequiredArgsConstructor
public class MessageController {

    private final MessageFacade messageFacade;

    @ApiOperation(value = "发送消息")
    @PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LXHLMessageResponse.SendMessage> platformLogin(@RequestBody MessageParam.SendMessage param) {
        return new HttpResult<>(messageFacade.sendMessage(param));
    }
}
