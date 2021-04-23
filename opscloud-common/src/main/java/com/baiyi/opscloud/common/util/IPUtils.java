package com.baiyi.opscloud.common.util;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:53 下午
 * @Since 1.0
 */
public class IPUtils {

    /**
     * 校验Ip格式 这是模仿js校验ip格式，使用java做的判断
     *
     * @param ip
     * @return
     */
    public static boolean checkIp(String ip) {
        String[] ipValue = ip.split("\\.");
        if (ipValue.length != 4) {
            return false;
        }
        for (String temp : ipValue) {
            try {
                // java判断字串是否整数可以用此类型转换异常捕获方法，也可以用正则 var regu = /^\d+$/;
                Integer q = Integer.valueOf(temp);
                if (q > 255) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
