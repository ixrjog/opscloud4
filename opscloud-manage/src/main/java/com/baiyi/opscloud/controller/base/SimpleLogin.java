package com.baiyi.opscloud.controller.base;

import com.baiyi.opscloud.domain.model.message.ILoginMessage;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/7/9 1:07 下午
 * @Version 1.0
 */
@Data
@Builder
public class SimpleLogin implements ILoginMessage {

    private String token;
}
