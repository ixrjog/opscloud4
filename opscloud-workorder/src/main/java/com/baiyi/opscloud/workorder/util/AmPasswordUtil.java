package com.baiyi.opscloud.workorder.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @Author baiyi
 * @Date 2023/4/18 16:34
 * @Version 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public class AmPasswordUtil {

    private AmPasswordUtil(){}

    private static final int PASSWORD_LENGTH = 8;

    private static final String NUM_CHAR = "23456789";
    private static final String UPPER_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CHAR = "abcdefghijklmnopqrstuvwxyz";
    private static final String SYBL_CHAR = "!@$%^&_+-=";

    public static String generatorRandomPassword() {
        Random random = new Random();
        String pwStr = getChar(random, NUM_CHAR, 1) +
                getChar(random, UPPER_CHAR, 1) +
                getChar(random, LOWER_CHAR, 1) +
                getChar(random, SYBL_CHAR, 1) +
                getChar(random, NUM_CHAR + UPPER_CHAR + LOWER_CHAR + SYBL_CHAR, PASSWORD_LENGTH - 4);
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

    private static String getChar(Random random, String sourceChar, int max) {
        //Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < max; i++) {
            int number = random.nextInt(sourceChar.length());
            sb.append(sourceChar.charAt(number));
        }
        return sb.toString();
    }

}