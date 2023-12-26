package com.baiyi.opscloud.core.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/4/12 13:47
 * @Version 1.0
 */
public class DingtalkUtil {

    private DingtalkUtil() {
    }

    /**
     *
     * @param name 钉钉 通讯录名称
     * @return Pair<ZH名, EN名>
     */
    public static Pair<String, String> cutDingtalkName(String name) {
        List<Character> hanStrs = Lists.newArrayList();
        List<Character> enStrs = Lists.newArrayList();
        for (char c : name.toCharArray()) {
            if (isHan(c)) {
                hanStrs.add(c);
            } else {
                enStrs.add(c);
            }
        }
        String zhName = Joiner.on("")
                .join(hanStrs)
                .trim();
        String enName = Joiner.on("")
                .join(enStrs)
                .replace("_", " ")
                .replaceAll(" {2,}", " ")
                .trim();
        return new ImmutablePair<>(zhName, enName);
    }

    static boolean isHan(char c) {
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        return sc == Character.UnicodeScript.HAN;
    }

}