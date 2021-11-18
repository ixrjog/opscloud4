package com.baiyi.opscloud.opscloud.provider;

import com.baiyi.opscloud.common.builder.SimpleDict;
import com.baiyi.opscloud.common.builder.SimpleDictBuilder;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.opscloud.http.OcHttpUtil;
import com.baiyi.opscloud.opscloud.vo.OcServerVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @Author baiyi
 * @Date 2021/6/24 10:32 上午
 * @Version 1.0
 */
@Component
public class OcServerProvider {

    private interface Api {
        String QUERY_SERVER_PAGE = "/server/page/query";
    }

    public DataTable<OcServerVO.Server> queryServers() throws IOException {
        SimpleDict param = SimpleDictBuilder.newBuilder()
                .putParam("page", "1")
                .putParam("length", "1000")
                .build();
        JsonNode jsonNode = OcHttpUtil.httpGetExecutor(OcServerProvider.Api.QUERY_SERVER_PAGE, param.getDict());
        if (jsonNode.get("success").asBoolean()) {
            Type type = new TypeToken<DataTable<OcServerVO.Server>>() {
            }.getType();
            String data = jsonNode.get("body").toString();
            return new GsonBuilder().create().fromJson(data, type);
        } else {
            return DataTable.EMPTY;
        }
    }
}
