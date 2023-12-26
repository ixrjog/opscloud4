package com.baiyi.opscloud.sshcore.message.base;

import com.baiyi.opscloud.domain.model.message.ILoginMessage;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/7/15 9:52 上午
 * @Version 1.0
 */
@Data
@Builder
public class SimpleLoginMessage implements ILoginMessage {

    private String token;

}