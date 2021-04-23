package com.baiyi.opscloud.cloud.ons;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.facade.aliyun.AliyunONSFacade;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/12 10:49 上午
 * @Since 1.0
 */
public class AliyunONSTest extends BaseUnit {

    @Resource
    private AliyunONSFacade aliyunONSFacade;

    @Test
    void taskTest(){
        aliyunONSFacade.onsGroupTask();
    }

}
