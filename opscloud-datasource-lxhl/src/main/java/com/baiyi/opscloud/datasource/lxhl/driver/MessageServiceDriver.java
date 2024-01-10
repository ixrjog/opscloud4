package com.baiyi.opscloud.datasource.lxhl.driver;

import com.baiyi.opscloud.common.datasource.LXHLConfig;
import com.baiyi.opscloud.datasource.lxhl.feign.MessageServiceFeign;
import feign.Feign;
import feign.Retryer;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2022/8/31 7:49 PM
 * @Since 1.0
 */

@Component
public class MessageServiceDriver {

    public String sendMessage(LXHLConfig.Account account, String mobile, String content) {
        MessageServiceFeign feign = Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .target(MessageServiceFeign.class, account.getUrl());
        return feign.sendMessage(account.getUsername(), account.getPassword(), mobile, content).replaceAll("\\p{C}", "");
    }

}