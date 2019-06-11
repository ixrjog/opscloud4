package com.sdg.cmdb.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by liangjian on 2017/4/6.
 */
public class PasswdUtils {

    private static final String passwdStr = "01234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String NUM_CHAR = "23456789"; // 8
    private static final String UPPER_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";  // 26
    private static final String LOWER_CHAR = "abcdefghijklmnopqrstuvwxyz";
    private static final String SYBL_CHAR = "!@#$%^&*()_+-="; //14




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


    public static String getPassword(int length) {
        if (length < 8) length = 8;
        String p = getChar(NUM_CHAR, 1);
        p += getChar(UPPER_CHAR, 1);
        p += getChar(LOWER_CHAR, 1);
        p += getChar(SYBL_CHAR, 1);
        p += getChar(NUM_CHAR + UPPER_CHAR + LOWER_CHAR + SYBL_CHAR, length - 4);
        return shuffleForSortingString(p);
    }

    public static String shuffleForSortingString(String s) {
        char[] c = s.toCharArray();
        List<Character> lst = new ArrayList<Character>();
        for (int i = 0; i < c.length; i++) {
            lst.add(c[i]);
        }
        //System.out.println(lst);
        Collections.shuffle(lst);
        //System.out.println(lst);
        String resultStr = "";
        for (int i = 0; i < lst.size(); i++) {
            resultStr += lst.get(i);
        }
        //System.out.println(resultStr);
        return resultStr;
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
        // int max = random.nextInt(maxLength) + 1;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < max; i++) {
            int number = random.nextInt(sourceChar.length());
            sb.append(sourceChar.charAt(number));
        }
        return sb.toString();
    }


}
