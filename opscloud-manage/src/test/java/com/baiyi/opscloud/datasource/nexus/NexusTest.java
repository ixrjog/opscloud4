package com.baiyi.opscloud.datasource.nexus;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.builder.SimpleDict;
import com.baiyi.opscloud.common.builder.SimpleDictBuilder;
import com.baiyi.opscloud.opscloud.http.OcHttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2021/8/5 6:12 下午
 * @Version 1.0
 */
public class NexusTest extends BaseUnit {


    @Test
    void ddd() {
        SimpleDict param = SimpleDictBuilder.newBuilder()
                .build();
        try {
            JsonNode jsonNode = OcHttpUtil.httpGetExecutor("http://maven.xinc818.com/service/rest/v1/assets", param.getDict());

            System.err.println(jsonNode.toString());
        } catch (IOException e) {
           e.printStackTrace();
        }

    }



}
