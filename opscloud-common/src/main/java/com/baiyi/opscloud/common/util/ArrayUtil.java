package com.baiyi.opscloud.common.util;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @Author baiyi
 * @Date 2023/7/26 09:24
 * @Version 1.0
 */
public class ArrayUtil {

    public static char[] sub(char[] array, int start, int end) {
        int length = Array.getLength(array);
        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }
        if (start == length) {
            return new char[0];
        } else {
            if (start > end) {
                int tmp = start;
                start = end;
                end = tmp;
            }
            if (end > length) {
                if (start >= length) {
                    return new char[0];
                }

                end = length;
            }
            return Arrays.copyOfRange(array, start, end);
        }
    }

}