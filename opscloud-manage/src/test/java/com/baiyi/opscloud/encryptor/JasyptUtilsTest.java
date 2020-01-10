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

    @Test
    void testEncrypt() {
        System.err.println(stringEncryptor.encrypt("123456"));
    }

    @Test
    void testDecrypt() {
        System.err.println(stringEncryptor.decrypt("11111"));
    }

    @Test
    void testEncryptPwd() {
        // 加密
        System.out.println(JasyptUtils.encryptPwd(PWD, "123456"));
    }

    @Test
    void testDecyptPwd() {
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
