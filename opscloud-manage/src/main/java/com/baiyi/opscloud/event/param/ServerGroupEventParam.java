package com.baiyi.opscloud.event.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/16 10:23 上午
 * @Since 1.0
 */
public class ServerGroupEventParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class delete {

        private Integer id;

    }
}
