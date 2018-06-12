package com.sdg.cmdb.service;


import com.sdg.cmdb.domain.keybox.ApplicationKeyDO;
import com.sdg.cmdb.domain.keybox.ApplicationKeyVO;
import com.sdg.cmdb.util.EncryptionUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


/**
 * Created by liangjian on 2017/1/20.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class KeyBoxServiceTest {

    @Resource
    private KeyBoxService keyBoxService;

    @Test
    public void test() {
        keyBoxService.checkUser();
    }

    @Test
    public void testDelUser() {
        System.err.println(keyBoxService.delUserKeybox("chen"));
    }

    @Test
    public void testGetwayStatus() {
        keyBoxService.getwayStatus("liangjian", "10.173.138.25");
    }


    @Test
    public void testKey2md5() {
        ApplicationKeyVO key = keyBoxService.getApplicationKey();

        System.err.println(key);

        System.err.println(EncryptionUtil.key2md5(key));
    }


}
