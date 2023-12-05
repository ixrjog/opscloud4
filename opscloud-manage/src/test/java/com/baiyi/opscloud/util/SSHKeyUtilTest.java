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
    void test() {
        String key = "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAICu9uF1Qly80peOl1PK42uJCOnOVpulFXRk9quqlBpcG";
        print(SSHUtil.getFingerprint(key));
    }

    @Test
    void test2() {
        String KEY = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDtVLKSMBSDxFBApa+1fmcGG0OHizL6kPfrFY7KMScoILNrhf5y2GoV5WxSSSd73c56YYd5HbfK3CFIjwZ54swDhKEiGkSDA7FOlriv1TTyvhknkDSsnsABibPKtRkP9XT3EzznolwikqWCbANTu1XiIR6EaX5r+rL54mtwE2xqOEKdkbU9wkkd41dEIMcwqcgazzTb3hrUunVFF5JrZXukCkLRRDGtYcXKA4vFOILpqLZiTMW7hPto3F9NGdBIy7ZphD2QUEuVmFgnwpCUb2ps0Ud3uLqdIF+folEHC4rqDBB2Nqgx/vbIB94U3bIJED9zkfOtubC5eUq7IFPrZvPb";
        print(SSHUtil.getKeyType(KEY));
    }

}
