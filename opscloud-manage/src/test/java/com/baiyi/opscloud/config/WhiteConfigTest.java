package com.baiyi.opscloud.config;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.config.properties.WhiteConfigurationProperties;
import com.baiyi.opscloud.task.service.QuartzService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @Author baiyi
 * @Date 2021/6/1 11:38 上午
 * @Version 1.0
 */
class WhiteConfigTest extends BaseUnit {

    @Resource
    private WhiteConfigurationProperties whiteConfig;

    @Resource
    private QuartzService quartzService;

    @Test
    void whiteConfigTest() {
        print(whiteConfig);
    }


    @Test
    void taskTest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", 1);
//        quartzService.deleteJob("job", "test");
        quartzService.pauseJob("job", "test");
//        quartzService.addJob(JobTest.class, "job", "test", "*/5 * * * * ?", map);
        while ( true ) {
            System.err.println("------");
            try {
                Thread.sleep(1000 * 30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}