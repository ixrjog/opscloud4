package com.baiyi.opscloud.tencent.exmail.handler;

import com.baiyi.opscloud.domain.param.tencent.ExmailParam;
import com.baiyi.opscloud.tencent.exmail.bo.TencentExmailUserBO;
import com.baiyi.opscloud.tencent.exmail.convert.TencentExmailUserConvert;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/29 2:17 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class TencentExmailUserHandler extends TencentExmailHandler {

    @Resource
    private TencentExmailTokenHandler tencentExmailTokenHandler;

    private interface userUrls {
        String createUser = "/cgi-bin/user/create";
        String getUser = "/cgi-bin/user/get";
        String updateUser = "/cgi-bin/user/update";
        String deleteUser = "/cgi-bin/user/delete";
    }

    public Boolean createUser(ExmailParam.User param) {
        String token = tencentExmailTokenHandler.getToken();
        String url = getWebHook(userUrls.createUser, token);
        try {
            JsonNode data = httpPostExecutor(url, param);
            if (checkResponse(data))
                return true;
            log.error(data.get("errmsg").asText());
            return false;
        } catch (IOException e) {
            log.error("TencentExmail用户创建失败", e);
            return false;
        }
    }

    public TencentExmailUserBO getUser(String userId) {
        String token = tencentExmailTokenHandler.getToken();
        String url = Joiner.on("").join(getWebHook(userUrls.getUser, token)
                , "&userid="
                , userId);
        try {
            JsonNode data = httpGetExecutor(url);
            if (checkResponse(data))
                return TencentExmailUserConvert.toBO(data);
            log.error(data.get("errmsg").asText());
            return null;
        } catch (IOException e) {
            log.error("TencentExmail用户获取失败", e);
            return null;
        }
    }

    public Boolean updateUser(ExmailParam.User param) {
        String token = tencentExmailTokenHandler.getToken();
        String url = getWebHook(userUrls.updateUser, token);
        try {
            JsonNode data = httpPostExecutor(url, param);
            return checkResponse(data);
        } catch (IOException e) {
            log.error("TencentExmail用户更新失败", e);
            return false;
        }
    }

    public Boolean deleteUser(String userId) {
        String token = tencentExmailTokenHandler.getToken();
        String url = Joiner.on("").join(getWebHook(userUrls.deleteUser, token)
                , "&userid="
                , userId);
        try {
            JsonNode data = httpGetExecutor(url);
            if (checkResponse(data))
                return true;
            log.error(data.get("errmsg").asText());
            return false;
        } catch (IOException e) {
            log.error("TencentExmail用户删除失败", e);
            return null;
        }
    }

}
