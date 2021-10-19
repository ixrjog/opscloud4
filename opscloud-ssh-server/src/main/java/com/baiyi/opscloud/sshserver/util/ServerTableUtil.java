package com.baiyi.opscloud.sshserver.util;

import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2021/6/8 4:31 下午
 * @Version 1.0
 */
public class ServerTableUtil {

    public static String buildPagination(long totalNum, int page, int length) {
        int tp = 0;
        try {
            tp = (int) (totalNum - 1) / length + 1;
        } catch (Exception ignored) {
        }
        return Joiner.on(" ,").join("页码: " + page, "分页长度: " + length, "总页数: " + tp, "总数量: " + totalNum, "翻页< 上一页: b 下一页: n >");
    }

    public static String buildFooter(long totalNum, int page, int length) {
        int tp = 0;
        try {
            tp = (int) (totalNum - 1) / length + 1;
        } catch (Exception ignored) {
        }
        return Joiner.on(" ,").join("页码: " + page, "分页长度: " + length, "总页数: " + tp, "总数量: " + totalNum);
    }

}


