package com.baiyi.opscloud.common.util;

/**
 * @Author baiyi
 * @Date 2022/7/13 15:02
 * @Version 1.0
 */
public class DingtalkUtil {

    private static final String DING_WEB_HOOK = "https://oapi.dingtalk.com/robot/send?access_token=${TOKEN}";

    /**
     * 钉钉机器人WebHook地址
     *
     * @return
     */
    public static String getRobotWebHook(String token) {
        return DING_WEB_HOOK.replace("${TOKEN}", token);
    }

}
