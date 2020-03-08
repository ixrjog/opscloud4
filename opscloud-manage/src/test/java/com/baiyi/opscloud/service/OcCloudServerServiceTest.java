package com.baiyi.opscloud.service;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.base.CloudServerType;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.service.cloud.OcCloudServerService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 10:44 上午
 * @Version 1.0
 */
public class OcCloudServerServiceTest extends BaseUnit {

    @Resource
    private OcCloudServerService ocCloudserverService;

    @Test
    void testQueryOcCloudServerByType() {
        List<OcCloudServer> list = ocCloudserverService.queryOcCloudServerByType(CloudServerType.ZH.getType());
        System.err.println(JSON.toJSONString(list));
    }

    @Test
    void test() {
        String s ="GID_GGJ_TRAFFIC_IMPORT_CENTER_PLATFORM_TB_FULL_SHOP_ORDER_INFO_TEST";
        System.err.println(s.length());
    }

}
