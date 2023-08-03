package com.baiyi.opscloud.common.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Author baiyi
 * @Date 2023/8/2 17:14
 * @Version 1.0
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class OmTools {
    
    private static final String PW_STR = "01234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUM_CHAR = "0123456789";
    private static final String UPPER_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CHAR = "abcdefghijklmnopqrstuvwxyz";
    private static final String SYBL_CHAR = "!@#$%^&*()_+-=";

    /**
     * 必须包含三种及以上类型：大写字母、小写字母、数字、特殊符号。长度为8～32位。特殊字符包括! @ # $ % ^ & * () _ + - =
     *
     * @return Aliyun RDS password
     */
    public static String generatorAliyunRDSPassword() {
        final String syblChar = "!@#$%^&*()_+-=";
        String sb = getChar(NUM_CHAR) +
                getChar(UPPER_CHAR) +
                getChar(LOWER_CHAR) +
                getChar(syblChar) +
                getChar(PW_STR + syblChar, 28);
        return shuffleForSortingString(sb);
    }

    public static String generatorShortUUID() {
        return IdUtil.buildUUID();
    }

    public static String generatorUUID() {
        return IdUtil.toUUID(IdUtil.buildUUID());
    }

    private static String getChar(String sourceChar) {
        return getChar(sourceChar, 1);
    }

    private static String getChar(String sourceChar, int max) {
        Random random = new Random();
        return IntStream.range(0, max)
                .map(i -> random.nextInt(sourceChar.length()))
                .mapToObj(number -> String.valueOf(sourceChar.charAt(number)))
                .collect(Collectors.joining());
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

}
