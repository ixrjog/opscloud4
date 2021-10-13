package com.baiyi.opscloud.tencent.exmail.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baiyi.opscloud.common.datasource.config.DsTencentExmailConfig;
import com.baiyi.opscloud.tencent.exmail.bo.TencentExmailUserBO;
import com.baiyi.opscloud.tencent.exmail.http.TencentExmailHttpUtil;
import com.baiyi.opscloud.tencent.exmail.param.ExmailParam;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/12 3:40 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TencentExmailUserHandler {

    @Resource
    private TencentExmailHttpUtil tencentExmailHttpUtil;

    @Resource
    private TencentExmailTokenHandler tencentExmailTokenHandler;

    private interface UserApi {
        String CREATE = "/cgi-bin/user/create";
        String GET = "/cgi-bin/user/get";
        String UPDATE = "/cgi-bin/user/update";
        String DELETE = "/cgi-bin/user/delete";
        String LIST = "/cgi-bin/user/simplelist";
    }

    public Boolean createUser(DsTencentExmailConfig.Tencent config, ExmailParam.User param) {
        String token = tencentExmailTokenHandler.getToken(config);
        String url = tencentExmailHttpUtil.getWebHook(config, UserApi.CREATE, token);
        try {
            JsonNode data = tencentExmailHttpUtil.httpPostExecutor(url, param);
            return tencentExmailHttpUtil.checkResponse(data);
        } catch (IOException e) {
            log.error("TencentExmail用户创建失败", e);
        }
        return false;
    }

    public TencentExmailUserBO getUser(DsTencentExmailConfig.Tencent config, String userId) {
        String token = tencentExmailTokenHandler.getToken(config);
        String url = Joiner.on("").join(tencentExmailHttpUtil.getWebHook(config, UserApi.GET, token)
                , "&userid="
                , userId);
        try {
            JsonNode data = tencentExmailHttpUtil.httpGetExecutor(url);
            if (tencentExmailHttpUtil.checkResponse(data)) {
                return JSON.parseObject(data.toString(), TencentExmailUserBO.class);
            }
            log.error(data.get("errmsg").asText());
        } catch (IOException e) {
            log.error("TencentExmail用户获取失败", e);
        }
        return null;
    }

    public List<TencentExmailUserBO> listUser(DsTencentExmailConfig.Tencent config, Long departmentId) {
        String token = tencentExmailTokenHandler.getToken(config);
        String url = Joiner.on("").join(tencentExmailHttpUtil.getWebHook(config, UserApi.LIST, token)
                , "&department_id="
                , departmentId
                , "&fetch_child=1");
        try {
            JsonNode data = tencentExmailHttpUtil.httpGetExecutor(url);
            if (tencentExmailHttpUtil.checkResponse(data)) {
                return JSONArray.parseArray(data.get("userlist").toString(), TencentExmailUserBO.class);
            }
            log.error(data.get("errmsg").asText());
        } catch (IOException e) {
            log.error("TencentExmail用户获取失败", e);
        }
        return Collections.emptyList();
    }

    public Boolean updateUser(DsTencentExmailConfig.Tencent config, ExmailParam.User param) {
        String token = tencentExmailTokenHandler.getToken(config);
        String url = tencentExmailHttpUtil.getWebHook(config, UserApi.UPDATE, token);
        try {
            JsonNode data = tencentExmailHttpUtil.httpPostExecutor(url, param);
            return tencentExmailHttpUtil.checkResponse(data);
        } catch (IOException e) {
            log.error("TencentExmail用户更新失败", e);
        }
        return false;
    }

    public Boolean deleteUser(DsTencentExmailConfig.Tencent config, String userId) {
        String token = tencentExmailTokenHandler.getToken(config);
        String url = Joiner.on("").join(tencentExmailHttpUtil.getWebHook(config, UserApi.DELETE, token)
                , "&userid="
                , userId);
        try {
            JsonNode data = tencentExmailHttpUtil.httpGetExecutor(url);
            return tencentExmailHttpUtil.checkResponse(data);
        } catch (IOException e) {
            log.error("TencentExmail用户删除失败", e);
        }
        return false;
    }
}
