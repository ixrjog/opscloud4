package com.baiyi.opscloud.cloud.server;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.cloud.server.factory.CloudServerFactory;
import com.baiyi.opscloud.tencent.cloud.cvm.handler.TencentCloudCVMHandler;
import com.baiyi.opscloud.tencent.cloud.cvm.instance.CVMInstance;
import com.tencentcloudapi.cvm.v20170312.models.Instance;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/20 6:02 下午
 * @Version 1.0
 */
public class TencentCVMCloudServerTest extends BaseUnit {

    @Resource
    private TencentCloudCVMHandler tencentCloudCVMHandler;

    private static final String key = "TencentCVMCloudserver";

    private ICloudServer getICloudServer() {
        return CloudServerFactory.getCloudServerByKey(key);
    }

    @Test
    void testGetInstanceList() {
        List<CVMInstance> list= tencentCloudCVMHandler.getInstanceList();
        System.err.println(list);
    }
    @Test
    void testGetInstance() {
        Instance i=  tencentCloudCVMHandler.getInstance("ins-qgaxfm1v");
        System.err.println(i);
    }
}
