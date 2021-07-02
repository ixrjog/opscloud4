package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.SSHUtil;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2021/7/2 4:04 下午
 * @Version 1.0
 */
public class SSHKeyUtilTest extends BaseUnit {

    @Test
    void test(){

        String key = "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAICu9uF1Qly80peOl1PK42uJCOnOVpulFXRk9quqlBpcG";
        SSHUtil.getFingerprint(key);
    }



}
