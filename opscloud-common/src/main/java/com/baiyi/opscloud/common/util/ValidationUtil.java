package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.domain.ErrorEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author baiyi
 * @Date 2020/1/7 1:52 下午
 * @Version 1.0
 */
public class ValidationUtil {

    private interface RegexMatches {
        String PHONE = "^1[3456789]\\d{9}$";
        String USERNAME = "[a-zA-Z][\\d0-9a-zA-Z-_]{3,64}";
        String SERVER_NAME = "[a-zA-Z][\\d0-9a-zA-Z-.]{1,55}";
        String SERVER_GROUP_NAME = "group_[a-z][\\d0-9a-z-]{2,64}";
        String APPLICATION_NAME = "[a-z][\\d0-9a-z-]{3,32}";
        String EMAIL = "[A-z0-9-_.]+@(\\w+[\\w-]*)(\\.\\w+[-\\w]*)+";
        String JOB_KEY = "[a-z0-9-_]{3,64}";
    }

    /**
     * 校验字符串是否为手机号
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        return phone.matches(RegexMatches.PHONE);
    }

    /**
     * 验证用户名，4-16位,不能包含中文 a-z 0-9 A-Z 不允许数字开头
     *
     * @param username
     * @return
     */
    public static void isUsernameRule(String username) {
        if (!username.matches(RegexMatches.USERNAME)) {
            throw new OCException(ErrorEnum.USER_USERNAME_NON_COMPLIANCE_WITH_RULES);
        }
    }

    /**
     * 校验服务器组名称
     *
     * @param serverGroupName
     * @return
     */
    public static void tryServerGroupNameRule(String serverGroupName) {
        if (!serverGroupName.matches(RegexMatches.SERVER_GROUP_NAME)) {
            throw new OCException(ErrorEnum.SERVERGROUP_NAME_NON_COMPLIANCE_WITH_RULES);
        }
    }

    public static void tryServerNameRule(String serverName) {
        if (!serverName.matches(RegexMatches.SERVER_NAME)) {
            throw new OCException(ErrorEnum.SERVER_NAME_NON_COMPLIANCE_WITH_RULES);
        }
    }

    public static boolean isEmail(String email) {
        return email.matches(RegexMatches.EMAIL);
    }

    public static boolean isApplication(String application) {
        return application.matches(RegexMatches.APPLICATION_NAME);
    }

    private static final String SPECIAL_SIGNS = "@!#$%^&*()_=+-[]|:;,.<>?";


    public static void checkPasswordRule(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new OCException("密码不能为空");
        }

        if (password.length() < 8 || password.length() > 30) {
            throw new OCException("密码长度需在8-30个字符之间");
        }

        String errCommon = "数字、大小写字母及特殊字符 " + SPECIAL_SIGNS;

        boolean hasNum = false, hasBigChar = false, hasSmallChar = false, hasSign = false;
        for (int i = 0; i < password.length(); i++) {
            int codeInt = password.charAt(i);
            if (48 <= codeInt && codeInt <= 57) {
                hasNum = true;
            } else if (65 <= codeInt && codeInt <= 90) {
                hasBigChar = true;
            } else if (97 <= codeInt && codeInt <= 122) {
                hasSmallChar = true;
            } else if (SPECIAL_SIGNS.contains(password.substring(i, i + 1))) {
                hasSign = true;
            } else {
                throw new RuntimeException("密码只能包含" + errCommon);
            }
        }
        if (!hasNum || !hasBigChar || !hasSmallChar || !hasSign) {
            throw new OCException("密码需要同时包含" + errCommon);
        }
    }

    @Deprecated
    public static String getHost(String url) {
        if (url == null || url.trim().isEmpty()) {
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

    public static boolean checkAppLabelInService(String serviceName, String label) {
        String repx = "(.*)-" + label + "$";
        return serviceName.matches(repx);
    }

    public static boolean checkApk(String fileName) {
        return fileName.endsWith(".apk");
    }

    public static boolean checkJar(String fileName) {
        return fileName.endsWith(".jar");
    }

    /**
     * 验证jobKey，3-64位,不能包含中文 a-z -
     *
     * @param key
     * @return
     */
    public static boolean isJobKeyRule(String key) {
        if (key.endsWith("-") || key.startsWith("-")) {
            return false;
        }
        return key.matches(RegexMatches.JOB_KEY);
    }

}
