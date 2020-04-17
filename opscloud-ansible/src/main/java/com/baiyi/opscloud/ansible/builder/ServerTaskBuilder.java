package com.baiyi.opscloud.ansible.builder;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.ansible.bo.ServerTaskBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.OcServerTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/7 9:13 下午
 * @Version 1.0
 */
public class ServerTaskBuilder {

    public static  OcServerTask build(OcUser ocUser, Map<String, String> serverTreeHostPatternMap, Object executorParam) {
        ServerTaskBO serverTaskBO = ServerTaskBO.builder()
                .userId(ocUser.getId())
                .userDetail(JSON.toJSONString(ocUser))
                .executorParam(JSON.toJSONString(executorParam))
                .serverTargetDetail(JSON.toJSONString(serverTreeHostPatternMap))
                .build();

        return covert(serverTaskBO);
    }

    private static  OcServerTask covert( ServerTaskBO serverTaskBO) {
        return  BeanCopierUtils.copyProperties(serverTaskBO, OcServerTask.class);
    }
}
