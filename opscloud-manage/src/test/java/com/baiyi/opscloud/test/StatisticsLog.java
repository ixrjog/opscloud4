package com.baiyi.opscloud.test;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/4/26 14:20
 * @Version 1.0
 */
public class StatisticsLog {

    @Builder
    @Data
    public static class Log {

        @Builder.Default
        private int status200 = 0;

        @Builder.Default
        private int statusOther = 0;

        @Builder.Default
        private List<Integer> rts = Lists.newArrayList();

    }
}
