package com.sdg.cmdb.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Base64;


/**
 * Created by zxxiao on 2016/11/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class EncryptionUtilTest {


    @Test
    public void testencrypt() {
        String pwd = EncryptionUtil.encrypt("-----BEGIN RSA PRIVATE KEY-----\n" +
                "FUCKogIBAAKCAQEAzuswWPArXG6aShTC3ax63+pYOgYYe51YeICqBzOmcAv8MXWD\n" +
                "wIQR/xlpzLQVlW198RKiOnvAt/8YWIJ261XnxhGM/zNpKM0ZWBRCNqsAC6XKJbPC\n" +
                "sY9SQASaKAmhB9m8IQzQvKOd0MxVmZ8FGDQdggYEtXqaUI3ypcc79A0bY53CJJI3\n" +
                "FEZYZ8uVwxDy1B4fLQAzBadxqxmVBtGpP+GRHKzc6RkUoiNvBWY+mVUEdBe+auRR\n" +

                "gG2ZZA5MqapMs5BtMU+rAoGAEwh5H1jlKUuc5tmbeL744mkAClsknW3/o8SuxNCj\n" +
                "MNJfGMmyOuZvKS2AHEYUh9ecrL+TfWVO7eezXPHvkS6DbEwgcAGAQ4cyTSVZpNYL\n" +
                "fgjeI3fn9/V031+f7Xn81F17go8F25zHo1EF/vg/N0NlPwQCphIhwaH/Qn1aRxAE\n" +
                "zZ0CgYEAti0Fne7isUzcS3RfZn2/uMHKglwSn/FLTrkdNtElTd58U78iV8tsGKg0\n" +
                "nlZ9ppZ3ZlOU91OE6LFDgV7yQe1YwWvKUFgXvzRDi/Y6zw+9ufaW9RobYajxOkeh\n" +
                "aakZWj7qbK16/88PTP8Sk91OzIWF5TSpW834/Yua/SckAdYPYcU=\n" +
                "-----END RSA PRIVATE KEY-----\n");
        System.err.println(pwd);
    }

    @Test
    public void testencryptByToy() {
        String pwd = EncryptionUtil.encrypt("-----BEGIN RSA PRIVATE KEY-----\n" +
                "FUCKoQIBAAKCAQEAsYyt2cnUkg3CcUtgWtTlb9MWXc/ub5dTgF4HjJferQp6NHzl\n" +
                "DvTZzVM3AUi2ZCoz7KuFZ22yRvAN69/1lUs4sk/V73kxFmuN/6ZziquAptypqoEN\n" +
                "L8FypPSpcfAU1d1HMtbI/XKQcMbHkxI9jyiAFHHHJkzwYB7LYEWUcTr0iJbBgYO6\n" +
                "/b6tBnXacx1IoPLF2fPov2D4W6NlYrF4JlviEA504dDYky0YQHrOpqDEPhEVU1ON\n" +
                "c6FVXNeXbxdONyH5gcpLgR8g0nHXPlC4phhow0CAzZLxAxEFSDwppCms36NLUaCc\n" +
                "c9wcI8bsA68ELL2vvP7qMWraqAk+rCfg2qRC0QIBIwKCAQEAnUIWUy8bXMpbw26l\n" +
                "zMsxnY8Tz28GYtZ9KI3MHBF8FZuQz2dOiZcKDaF53JDcD5MYDCLj30PnC6FraoSJ\n" +
                "DzP/BFVXFe7/l4PViqUfBcsaLWRbwudU0obEoLtxgixqOcP9O6hLnqdMu6jOc6Jx\n" +

                "ZlNsXw0x1sTHTQchuQ0e6cgem/rcTJHqT8stp1AcEgenf20K7rH/hJhpa/UEksnY\n" +
                "KJ/uMFtTfSiAp4JV8d55nd3vH8qkrK2MgdJbgUSthtqzZMTlxZQUiyNUaX72qoxF\n" +
                "XPlpWK1upUJHFBdDVcvLAoGAEbADKkYQkDstlS1+zyN3mhPWcspOy2l1TiMkzywX\n" +
                "sFvpV44Pi5cScGrLgo+95auum3n1S/4MaLmRy78D94qnrGx2Y0wKKz6l/nZBjLxM\n" +
                "mlHt3KdU/wHQi/mTwVz7YaDQVzqb4q7EuJ6elV7leLg5b3uGwq8TDWp8a74ZY8Y9\n" +
                "oJsCgYAdQ5QQFrkXVG/DK4hcUwb8CeOu30PQBaAEyN/3qIQBGFeJ4RMd4/iCwdSv\n" +
                "HGq/61cBe/mR2v7XXf3vFD3ea+vF7sFZTh1pYsHn/mK8Go5c4utqakatY+LLcHE4\n" +
                "XSIoTLTx3Kwt1roUXwlyHp8VoxGKyznBZ72/6NQrw4nuiV459U==\n" +
                "-----END RSA PRIVATE KEY-----\n");
        System.err.println(pwd);
    }


    @Test
    public void testdecrypt() {
        String pwd = "Yzb0133Cfgy+rju6twlAZER8IG66j39gIpQwJi1mrh7YzBlRO49wA9fwus5KRzDaOOhqinis42k/QacpPjV6z02ck9CFhGQo9hv5aRjjmj+" +
                "aMv2shyB4n7khDcosZjdJcSIA95mwD1mJiopXX7Hz";
        String result = EncryptionUtil.decrypt(pwd);

        System.err.println(result);
    }

    @Test
    public void testSs() {
        String key = "aes-256-cfb:SuqrDR3CMVGRNF2Wv2i3@hk-ss1.ops.yangege.cn:22407";
        try {
            String qcode = EncryptionUtil.getSsQcode(key);
            System.err.println(qcode);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testFingerprint() {
        String sss = "a  b  c d";
        String[] ss = sss.split(" +");
        System.err.println(ss.length);
        for (String s : ss)
            System.err.println(s);

    }


    @Test
    public void test2() {
        String id_rsa_pub = "eyJhbGciOiJIUzI1NiJ9.InNzaC1yc2EgQUFBQUIzTnphQzF5YzJFQUFBQUJJd0FBQVFFQXM1VkdNelVmL2JRYmtGM2E4V2M5NjVUWS9oQmZNZGdyNCtOczhISWZaWjFORUl5a1hEZ3BtbDNnQzhkQm1FbUJ1K3Bkekw5MndaVFFFT2E3bTkzdXNiYlhUUzZoZTZIMkphRzNlcDRwbGlTYmluNEUyUzBxQUlQZm10bzI0ajJYOGZjTlBwSW82YmtlSUs0TjM4NHRtMXluaFdialhraVgzMHAyNjBwSGJFUEtNdmhWVVlhdzdXSnFDS2tOMTVVZEtQakE4Q2c5S3lYdTFQMUorNnJzeUYzVk9QK1JsNXczMHB2K083andmSEJrZCtRVWlha2YrYUV0NGZUZVRyTyt5c3c0RWR1UDZTY2d0MnpoRWpjbEgzdE5NbEt2TGZEc2R4Tk0xTUhQdVcxVTBORlV3Y01UT2xpWGNKSmJ2OXdaV3dnSUM3TERSUVFoZzNyeCtCVEQwUT09IGxpYW5namlhbkA1MXhpYW5xdS5uZXQi.sh20VPQSBmGSjfSey-ZYc5YISuVdlqi7BkOh3rXzccY";

        try {

            byte[] x = new BASE64Decoder().decodeBuffer(id_rsa_pub);
            String x2 = new String(x);
            System.err.println(x2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test99() {
        String pwd = EncryptionUtil.encrypt("BadPaddingException: Decryption error");
        System.err.println(pwd);


        String result = EncryptionUtil.decrypt(pwd);

        System.err.println(result);
    }


}
