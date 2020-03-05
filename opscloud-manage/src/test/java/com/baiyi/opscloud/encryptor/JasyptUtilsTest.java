package com.baiyi.opscloud.encryptor;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.zabbix.util.encryptor.JasyptUtils;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2019/12/26 9:58 上午
 * @Version 1.0
 */
public class JasyptUtilsTest extends BaseUnit {

    private String PWD = "123456";

    @Resource
    private StringEncryptor stringEncryptor;

    /**
     * 加密
     */
    @Test
    void testEncrypt() {
        System.err.println(stringEncryptor.encrypt(""));
    }

    /**
     * 解密
     */
    @Test
    void testDecrypt() {
        System.err.println(stringEncryptor.decrypt(""));
    }

    @Test
    void testEncryptPwd() {
        // 加密
        System.out.println(JasyptUtils.encryptPwd(PWD, "123456"));
    }

    @Test
    void testDecyptPwd() {

        String p = "2vym+ky!997d5kkcc64mnz06y1mmui3lut#(^wd=%s_qj$1%x";
        String a = "eyJhbGciOiJIUzI1NiJ9.InNzaC1yc2EgQUFBQUIzTnphQzF5YzJFQUFBQURBUUFCQUFBQkFRRHRWTEtTTUJTRHhGQkFwYSsxZm1jR0cwT0hpekw2a1BmckZZN0tNU2NvSUxOcmhmNXkyR29WNVd4U1NTZDczYzU2WVlkNUhiZkszQ0ZJandaNTRzd0RoS0VpR2tTREE3Rk9scml2MVRUeXZoa25rRFNzbnNBQmliUEt0UmtQOVhUM0V6em5vbHdpa3FXQ2JBTlR1MVhpSVI2RWFYNXIrckw1NG10d0UyeHFPRUtka2JVOXdra2Q0MWRFSU1jd3FjZ2F6elRiM2hyVXVuVkZGNUpyWlh1a0NrTFJSREd0WWNYS0E0dkZPSUxwcUxaaVRNVzdoUHRvM0Y5TkdkQkl5N1pwaEQyUVVFdVZtRmdud3BDVWIycHMwVWQzdUxxZElGK2ZvbEVIQzRycURCQjJOcWd4L3ZiSUI5NFUzYklKRUQ5emtmT3R1YkM1ZVVxN0lGUHJadlBiIGJhaXlpQGdlZ2VqaWEuY29tIg.HETO-JsB8v47z8A4BVTY7_di3ym-ry3atkOoJ7Rv6UM";
        //System.out.println(JasyptUtils.decyptPwd(p, a));
        /**
         */
        // 解密  mysql
        System.out.println(JasyptUtils.decyptPwd(PWD, "8CG4i1d2w9NYgzXh2hMXTEzm4fBU72ghDnh5yqivkek="));
        // zabbix
        System.out.println(JasyptUtils.decyptPwd(PWD, "TuF9JGK3j4+ZEHmaUB3OiiOn9Vb0yRRLq92XrQQ3CwLBU2W3TrJnXX5hw8voL+ZB"));
        // ldap
        System.out.println(JasyptUtils.decyptPwd(PWD, "QSZbx+hCSXh68Pp0H9Z58a/psPfgeP9vMBlkj18q9BC4UcYE1u/J7NcX42LRctPl"));
    }


}
