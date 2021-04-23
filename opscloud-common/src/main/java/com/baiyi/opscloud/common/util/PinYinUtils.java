package com.baiyi.opscloud.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/16 11:23 上午
 * @Since 1.0
 */
public class PinYinUtils {

    // 设置汉字拼音输出的格式
    private static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

    static {
        // 不使用音调标记，
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // 如果使用WITH_TONE_MARK，则必须指定setVCharType为WITH_U_UNICODE，否则报错
        // fmt.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        // fmt.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        // 韵母“驴”(lu->lv)使用V来代替
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        // 返回的拼音为小字字母
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
    }

    // 中文转拼音
    public static String toPinYin(String text) {
        StringBuilder allPinYin = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char wordChar = text.charAt(i);
            // 如果为汉字
            if (Character.toString(wordChar).matches("[\\u4E00-\\u9FA5]")) {
                try {
                    // 返回汉字的全部拼音(因为有些汉字为多音字，否则只返回一个)
                    String[] pinYinArray = PinyinHelper.toHanyuPinyinStringArray(
                            wordChar, format);
                    // 取第一个拼音
                    String pinYin = pinYinArray[0];
                    if (pinYin != null) {
                        allPinYin.append(pinYin);
                    }
                } catch (Exception ignored) {
                }
            } else if (((int) wordChar >= 65 && (int) wordChar <= 90)
                    || ((int) wordChar >= 97 && (int) wordChar <= 122)) {
                allPinYin.append(wordChar);
            }
        }
        return allPinYin.toString();
    }


}
