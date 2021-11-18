package com.baiyi.opscloud.opscloud.provider;

import com.baiyi.opscloud.common.builder.SimpleDict;
import com.baiyi.opscloud.common.builder.SimpleDictBuilder;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.vo.auth.UserRoleVO;
import com.baiyi.opscloud.opscloud.http.OcHttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/29 10:17 上午
 * @Since 1.0
 */

@Component
public class OcAuthProvider {

    private interface Api {
        String QUERY_USER_ROLE_PAGE = "/auth/user/role/page/query";
    }

    public DataTable<UserRoleVO.UserRole> queryUserRolePage(Integer roleId) throws IOException {
        SimpleDict param = SimpleDictBuilder.newBuilder()
                .putParam("roleId", String.valueOf(roleId))
                .putParam("page", "1")
                .putParam("length", "1000")
                .build();
        JsonNode jsonNode = OcHttpUtil.httpGetExecutor(Api.QUERY_USER_ROLE_PAGE, param.getDict());
        if (jsonNode.get("success").asBoolean()) {
            Type type = new TypeToken<DataTable<UserRoleVO.UserRole>>() {
            }.getType();
            String data = jsonNode.get("body").toString();
            return new GsonBuilder().create().fromJson(data, type);
        } else {
            return DataTable.EMPTY;
        }
    }
}
