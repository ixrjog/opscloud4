package com.baiyi.caesar.opscloud.provider;

import com.baiyi.caesar.common.builder.SimpleDict;
import com.baiyi.caesar.common.builder.SimpleDictBuilder;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.opscloud.http.OcHttpUtil;
import com.baiyi.caesar.opscloud.vo.OcServerVO;
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
                .paramEntry("page", "1")
                .paramEntry("length", "1000")
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
