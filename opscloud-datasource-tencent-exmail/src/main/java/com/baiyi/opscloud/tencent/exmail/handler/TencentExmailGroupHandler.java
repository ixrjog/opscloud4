package com.baiyi.opscloud.tencent.exmail.handler;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.common.datasource.config.DsTencentExmailConfig;
import com.baiyi.opscloud.tencent.exmail.bo.TencentExmailGroupBO;
import com.baiyi.opscloud.tencent.exmail.http.TencentExmailHttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/13 10:37 上午
 * @Since 1.0
 */

@Slf4j
@Component
public class TencentExmailGroupHandler {

    @Resource
    private TencentExmailHttpUtil tencentExmailHttpUtil;

    @Resource
    private TencentExmailTokenHandler tencentExmailTokenHandler;

    private interface GroupApi {
        String GET = "/cgi-bin/group/get";
        String UPDATE = "/cgi-bin/group/update";
    }

    public TencentExmailGroupBO getGroup(DsTencentExmailConfig.Tencent config, String groupId) {
        String token = tencentExmailTokenHandler.getToken(config);
        String url = Joiner.on("").join(tencentExmailHttpUtil.getWebHook(config, GroupApi.GET, token)
                , "&groupid="
                , groupId);
        try {
            JsonNode data = tencentExmailHttpUtil.httpGetExecutor(url);
            if (tencentExmailHttpUtil.checkResponse(data)) {
                return JSON.parseObject(data.toString(), TencentExmailGroupBO.class);
            }
            log.error(data.get("errmsg").asText());
        } catch (IOException e) {
            log.error("TencentExmail群组获取失败", e);
        }
        return null;
    }
}
