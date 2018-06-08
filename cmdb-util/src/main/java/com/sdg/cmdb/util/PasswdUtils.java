package com.sdg.cmdb.util;

import java.util.Random;

/**
 * Created by liangjian on 2017/4/6.
 */
public class PasswdUtils {

    private static final String passwdStr = "01234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 生成随机密码
     *
     * @param length
     * @return
     */
    public static String getRandomPasswd(int length) {
        if (length == 0) length = 20;
        String str = passwdStr;
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


}
