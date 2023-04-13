package com.baiyi.opscloud.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author baiyi
 * @Date 2021/5/28 9:17 上午
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HostInfo {

    @Builder.Default
    private String hostAddress = "127.0.0.1";

    @Builder.Default
    private String hostname = "opscloud";

    public static HostInfo build() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            return HostInfo.builder()
                    .hostAddress(addr.getHostAddress())
                    .hostname(hostname)
                    .build();
        } catch (UnknownHostException ex) {
            return HostInfo.builder().build();
        }
    }

}