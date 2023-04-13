package com.baiyi.opscloud.common.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @Author baiyi
 * @Date 2020/1/7 1:52 下午
 * @Version 1.0
 */
public class PasswordUtil {

    private static final String PW_STR = "01234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUM_CHAR = "23456789";
    private static final String UPPER_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CHAR = "abcdefghijklmnopqrstuvwxyz";
    private static final String SYBL_CHAR = "!@#$%^&*()_+-=";


    /**
     * 生成密码
     *
     * @param length 密码长度
     * @param safe   安全 包含至少1个大写，1个小写，1个数字，1个特殊字符（长度必须大于8）
     * @return
     */
    public static String generatorPassword(int length, boolean safe) {
        if (safe) {
            return generatorSafePW(length);
        }
        return generatorPW(length);
    }

    /**
     * 生成随机密码
     *
     * @param length 密码长度
     * @return
     */
    public static String generatorPW(int length) {
        if (length == 0) {
            length = 20;
        }
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(PW_STR.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成随机密码 包含至少1个大写，1个小写，1个数字，1个特殊字符（长度必须大于8）
     *
     * @param length 长度>=8
     * @return
     */
    private static String generatorSafePW(int length) {
        if (length < 8) {
            length = 8;
        }
        String pwStr = getChar(NUM_CHAR, 1) +
                getChar(UPPER_CHAR, 1) +
                getChar(LOWER_CHAR, 1) +
                getChar(SYBL_CHAR, 1) +
                getChar(NUM_CHAR + UPPER_CHAR + LOWER_CHAR + SYBL_CHAR, length - 4);
        return shuffleForSortingString(pwStr);
    }

    private static String shuffleForSortingString(String s) {
        char[] c = s.toCharArray();
        List<Character> lst = Lists.newArrayList();
        for (char value : c) {
            lst.add(value);
        }
        Collections.shuffle(lst);
        return Joiner.on("").join(lst);
    }

    /**
     * 取随机长度，1 ～ maxLength
     *
     * @param sourceChar
     * @param max
     * @return
     */
    public static String getChar(String sourceChar, int max) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < max; i++) {
            int number = random.nextInt(sourceChar.length());
            sb.append(sourceChar.charAt(number));
        }
        return sb.toString();
    }

}