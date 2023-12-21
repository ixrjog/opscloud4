package com.baiyi.opscloud.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/4/6 10:14
 * @Version 1.0
 */
@Slf4j
public class IPUtil {

    private IPUtil() {
    }

    /**
     * @param network 192.168.5.100
     * @param mask    192.168.0.0/16
     * @return
     */
    public static boolean includeMask(String network, String mask) {
        if ("0.0.0.0/0".equals(mask) || "0.0.0.0".equals(mask)) {
            return true;
        }
        String[] networkIps = network.split("\\.");
        int ipAddr = (Integer.parseInt(networkIps[0]) << 24)
                | (Integer.parseInt(networkIps[1]) << 16)
                | (Integer.parseInt(networkIps[2]) << 8)
                | Integer.parseInt(networkIps[3]);
        int type = Integer.parseInt(mask.replaceAll(".*/", ""));
        int mask1 = 0xFFFFFFFF << (32 - type);
        String maskIp = mask.replaceAll("/.*", "");
        String[] maskIps = maskIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(maskIps[0]) << 24)
                | (Integer.parseInt(maskIps[1]) << 16)
                | (Integer.parseInt(maskIps[2]) << 8)
                | Integer.parseInt(maskIps[3]);
        return (ipAddr & mask1) == (cidrIpAddr & mask1);
    }

    public static boolean includeMasks(String network, List<String> masks) {
        // 不配置Regions则默认包含
        if (CollectionUtils.isEmpty(masks)) {
            return true;
        }
        for (String mask : masks) {
            try {
                if (IPUtil.includeMask(network, mask)) {
                    return true;
                }
            } catch (Exception e) {
                // 格式错误
                log.error(e.getMessage());
            }
        }
        return false;
    }

}