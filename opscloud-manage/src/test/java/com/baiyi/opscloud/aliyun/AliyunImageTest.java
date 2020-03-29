package com.baiyi.opscloud.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.ecs.handler.AliyunImageHandler;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/17 4:36 下午
 * @Version 1.0
 */
public class AliyunImageTest extends BaseUnit {

    @Resource
    private AliyunImageHandler aliyunImageHandler;

    @Resource
    private AliyunCore aliyunCore;

    @Test
    void testGetImage() {
        for (String regionId : aliyunCore.getRegionIds()) {
            List<DescribeImagesResponse.Image> list = aliyunImageHandler.getImageList(regionId, true);
            System.err.println(JSON.toJSONString(list));
        }

    }

}
