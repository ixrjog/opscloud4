package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/24 11:38 上午
 * @Version 1.0
 */
@Data
@Builder
public class ServerAddr {
    @Builder.Default
    private String hostAddress = "127.0.0.1";
    @Builder.Default
    private String hostname = "opscloud";
}
