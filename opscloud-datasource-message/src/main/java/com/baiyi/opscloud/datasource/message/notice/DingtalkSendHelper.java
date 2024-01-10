package com.baiyi.opscloud.datasource.message.notice;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.datasource.message.DingtalkMsg;
import com.baiyi.opscloud.datasource.message.feign.DingtalkRobotSendFeign;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/5/31 20:48
 * @Version 1.0
 */
@Slf4j
@Component
public class DingtalkSendHelper {

    private static final String DINGTALK_OAPI = "https://oapi.dingtalk.com";

    private DingtalkRobotSendFeign buildFeign() {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(DingtalkRobotSendFeign.class, DINGTALK_OAPI);
    }

    public void send(ZabbixConfig.Zabbix config, String message) {
        if (config.getNotice() == null) {
            return;
        }
        DingtalkRobotSendFeign feign = buildFeign();
        Gson gson = new GsonBuilder().create();
        DingtalkMsg.Msg msg = gson.fromJson(message, DingtalkMsg.Msg.class);
        Object result = feign.send(config.getNotice().getToken(), msg);
        log.debug(JSONUtil.writeValueAsString(result));
    }

    public void send(String token, String message) {
        DingtalkRobotSendFeign feign = buildFeign();
        Gson gson = new GsonBuilder().create();
        DingtalkMsg.Msg msg = gson.fromJson(message, DingtalkMsg.Msg.class);
        Object result = feign.send(token, msg);
        log.debug(JSONUtil.writeValueAsString(result));
    }

}