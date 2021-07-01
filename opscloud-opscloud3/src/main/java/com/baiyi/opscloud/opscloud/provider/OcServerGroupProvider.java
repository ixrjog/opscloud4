package com.baiyi.opscloud.opscloud.provider;

import com.baiyi.opscloud.common.builder.SimpleDict;
import com.baiyi.opscloud.common.builder.SimpleDictBuilder;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.opscloud.http.OcHttpUtil;
import com.baiyi.opscloud.opscloud.vo.OcServerGroupTypeVO;
import com.baiyi.opscloud.opscloud.vo.OcServerGroupVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @Author baiyi
 * @Date 2021/6/24 9:16 上午
 * @Version 1.0
 */
@Component
public class OcServerGroupProvider {

    private interface Api {
        String QUERY_SERVERGROUP_TYPE_PAGE = "/server/group/type/page/query";
        String QUERY_SERVERGROUP_PAGE = "/server/group/page/query";
    }

    public DataTable<OcServerGroupTypeVO.ServerGroupType> queryServerGroupTypes() throws IOException {
        SimpleDict param = SimpleDictBuilder.newBuilder()
                .paramEntry("page", "1")
                .paramEntry("length", "1000")
                .build();
        JsonNode jsonNode = OcHttpUtil.httpGetExecutor(OcServerGroupProvider.Api.QUERY_SERVERGROUP_TYPE_PAGE, param.getDict());
        if (jsonNode.get("success").asBoolean()) {
            Type type = new TypeToken<DataTable<OcServerGroupTypeVO.ServerGroupType>>() {
            }.getType();
            String data = jsonNode.get("body").toString();
            return new GsonBuilder().create().fromJson(data, type);
        } else {
            return DataTable.EMPTY;
        }
    }

    public DataTable<OcServerGroupVO.ServerGroup> queryServerGroups() throws IOException {
        SimpleDict param = SimpleDictBuilder.newBuilder()
                .paramEntry("page", "1")
                .paramEntry("length", "1000")
                .build();
        JsonNode jsonNode = OcHttpUtil.httpGetExecutor(OcServerGroupProvider.Api.QUERY_SERVERGROUP_PAGE , param.getDict());
        if (jsonNode.get("success").asBoolean()) {
            Type type = new TypeToken<DataTable<OcServerGroupVO.ServerGroup>>() {
            }.getType();
            String data = jsonNode.get("body").toString();
            return new GsonBuilder().create().fromJson(data, type);
        } else {
            return DataTable.EMPTY;
        }
    }
}
