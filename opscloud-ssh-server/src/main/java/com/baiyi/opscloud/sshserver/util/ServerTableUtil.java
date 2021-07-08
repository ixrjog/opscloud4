package com.baiyi.opscloud.sshserver.util;

import com.baiyi.opscloud.sshserver.command.util.TableHeaderBuilder;
import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2021/6/8 4:31 下午
 * @Version 1.0
 */
public class ServerTableUtil {

   // public static final String DIVIDING_LINE = "------+---------------------------------+---------------------------------+------------+--------------------------------+-------------------------------------";

    public static final String TABLE_HEADERS = buildTableHeaders();

    public static String buildTableHeaders() {
        return TableHeaderBuilder.newBuilder()
                .addHeader("ID", 4)
                .addHeader("Server Name", 32)
                .addHeader("ServerGroup Name", 32)
                .addHeader("Env", 11)
                .addHeader("IP", 31)
                .addHeader("Account", 25)
                .build();
    }

    public static String buildPagination(long totalNum,int page, int length) {
        int tp = 0;
        try {
            tp = (int) (totalNum - 1) / length + 1;
        } catch (Exception ignored) {
        }
        return Joiner.on(" ,").join("页码: " + page, "分页长度: " + length, "总页数: " + tp, "总数量: " + totalNum, "翻页< 上一页: b 下一页: n >");
    }

}


