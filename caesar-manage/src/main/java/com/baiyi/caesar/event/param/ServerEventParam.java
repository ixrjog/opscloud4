package com.baiyi.caesar.event.param;

import com.baiyi.caesar.domain.generator.caesar.Server;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/15 5:56 下午
 * @Since 1.0
 */
public class ServerEventParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class update {

        private Server server;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class delete {

        private Integer id;

    }
}
