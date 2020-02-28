package com.baiyi.opscloud.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author baiyi
 * @Date 2020/1/7 1:52 下午
 * @Version 1.0
 */
public class RegexUtils {

    /**
     * 校验字符串是否为手机号
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,1,2,5-9])|(177))\\d{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    /**
     * 验证用户名，4-16位,不能包含中文 a-z 0-9 A-Z 不允许数字开头
     *
     * @param username
     * @return
     */
    public static boolean isUsernameRule(String username) {
        return username.matches("[a-zA-Z][\\w]{3,15}");
    }

    public static boolean isApplicationNameRule(String appName){
        return appName.matches("[a-z][\\d0-9a-z-]{2,24}");
    }

    /**
     * 校验服务器组名称
     * @param serverGroupName
     * @return
     */
    public static boolean isServerGroupNameRule(String serverGroupName){
        return serverGroupName.matches("group_[a-z][\\d0-9a-z-]{2,64}");
    }

    public static boolean isUserGroupNameRule(String userGroupName){
        return isServerNameRule(userGroupName);
    }

    public static boolean isServerNameRule(String serverName){
        return serverName.matches("[a-z][\\d0-9a-z-]{1,32}");
    }

    public static boolean isEmail(String email) {
        String repx = "\\w+@([\\w]+[\\w-]*)(\\.[\\w]+[-\\w]*)+";
        return email.matches(repx);
    }

    /**
     * 校验密码强度 包含大写,小写,特殊字符,长度至少8:
     *
     * @param password
     * @return
     */
    public static boolean checkPasswordRule(String password) {
        String repx = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
        return password.matches(repx);
    }


    public static String getHost(String url) {
        if (url == null || url.trim().equals("")) {
            return "";
        }
        String host = "";
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+(:\\d*)?");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            host = matcher.group();
        }
        return host;
    }


    public static boolean checkAppLabelInService(String serviceName, String label){
        String repx = "(.*)-" + label + "$";
        return serviceName.matches(repx);
    }


}
