package com.baiyi.opscloud.leo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/7/24 17:22
 * @Version 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopBuildFlag {

    public static final StopBuildFlag NOT_STOP = StopBuildFlag.builder().build();

    @Builder.Default
    private Boolean isStop = false;

    private String username;

}