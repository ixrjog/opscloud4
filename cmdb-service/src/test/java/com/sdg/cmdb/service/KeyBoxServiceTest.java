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
        keyBoxService.getwayStatus("manage", "10.17.1.152");
    }


    @Test
    public void testKey2md5() {
        ApplicationKeyVO key = keyBoxService.getApplicationKey();

        System.err.println(key);

        System.err.println(EncryptionUtil.key2md5(key));

        System.err.println(EncryptionUtil.decrypt(key.getPrivateKey()));

        String privateKey =EncryptionUtil.decrypt(key.getPrivateKey());

        //System.err.println(EncryptionUtil.md5(key.getOriginalPrivateKey()));
        System.err.println("------------");
        String[] ss= privateKey.split("\n");
        for(String s:ss){
            System.err.println(s);
            System.err.println(s.length());
        }

    }

    @Test
    public void testKey() {
         System.err.println(EncryptionUtil.decrypt("key"));
    }

    @Test
    public void testFingerprint() {
        String s = "980AC217C6B51E7DC41040BEC1EDFEC8";
        String r=EncryptionUtil.fingerprint(s);
        System.err.println(r);

    }

}
