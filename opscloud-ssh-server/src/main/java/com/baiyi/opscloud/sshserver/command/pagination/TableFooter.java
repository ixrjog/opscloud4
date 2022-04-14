package com.baiyi.opscloud.sshserver.command.pagination;

import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/4/14 13:29
 * @Version 1.0
 */
public class TableFooter {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pagination {

        private long totalNum;
        private int page;
        private int length;

        // 需要分页
        @Builder.Default
        private boolean needPageTurning = false;

        @Builder.Default
        public String pageTurning = "翻页< 上一页: b 下一页: n >";

        public void print(SshShellHelper helper, PromptColor color) {
            int tp = 0;
            try {
                tp = (int) (totalNum - 1) / length + 1;
            } catch (Exception ignored) {
            }
            String p = Joiner.on(" ,").skipNulls().join(
                    "页码: " + page,
                    "分页长度: " + length,
                    "总页数: " + tp,
                    "总数量: " + totalNum,
                    needPageTurning ? pageTurning : null
            );
            helper.print(p, color);
        }

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Footer {

        private long totalNum;
        private int page;
        private int length;

        public void print(SshShellHelper helper, PromptColor color) {
            int tp = 0;
            try {
                tp = (int) (totalNum - 1) / length + 1;
            } catch (Exception ignored) {
            }
            String f = Joiner.on(" ,").join("页码: " + page, "分页长度: " + length, "总页数: " + tp, "总数量: " + totalNum);
            helper.print(f, color);
        }
    }


}
