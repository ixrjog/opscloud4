package com.baiyi.opscloud.service;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.cloudserver.base.CloudserverType;
import com.baiyi.opscloud.domain.generator.OcCloudserver;
import com.baiyi.opscloud.service.server.OcCloudserverService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 10:44 上午
 * @Version 1.0
 */
public class OcCloudserverServiceTest extends BaseUnit {

    @Resource
    private OcCloudserverService ocCloudserverService;

    @Test
    void testQueryOcCloudserverByType() {
        List<OcCloudserver> list = ocCloudserverService.queryOcCloudserverByType(CloudserverType.ZH.getType());
        System.err.println(JSON.toJSONString(list));
    }

}
