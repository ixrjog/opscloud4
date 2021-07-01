package com.baiyi.opscloud.opscloud.provider;

import com.baiyi.opscloud.common.builder.SimpleDict;
import com.baiyi.opscloud.common.builder.SimpleDictBuilder;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.opscloud.http.OcHttpUtil;
import com.baiyi.opscloud.opscloud.vo.OcUserVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @Author baiyi
 * @Date 2021/6/23 5:13 下午
 * @Version 1.0
 */
@Component
public class OcUserProvider {

    private interface Api {
        String QUERY_USER_PAGE = "/user/page/query";
        String QUERY_USER_DETAIL = "/user/detail/query";
    }

    public OcUserVO.User queryUserDetail(String username) throws IOException {
        SimpleDict param = SimpleDictBuilder.newBuilder()
                .paramEntry("username", username)
                .build();
        JsonNode jsonNode = OcHttpUtil.httpGetExecutor(Api.QUERY_USER_DETAIL, param.getDict());
        if (jsonNode.get("success").asBoolean()) {
            Type type = new TypeToken<OcUserVO.User>() {
            }.getType();
            String data = jsonNode.get("body").toString();
            return new GsonBuilder().create().fromJson(data, type);
        } else {
            return null;
        }
    }

    public DataTable<UserVO.User> queryUsers() throws IOException {
        SimpleDict param = SimpleDictBuilder.newBuilder()
                .paramEntry("page", "1")
                .paramEntry("length", "1000")
                .build();
        JsonNode jsonNode = OcHttpUtil.httpGetExecutor(Api.QUERY_USER_PAGE, param.getDict());
        if (jsonNode.get("success").asBoolean()) {
            Type type = new TypeToken<DataTable<UserVO.User>>() {
            }.getType();
            String data = jsonNode.get("body").toString();
            return new GsonBuilder().create().fromJson(data, type);
        } else {
            return DataTable.EMPTY;
        }
    }


}
