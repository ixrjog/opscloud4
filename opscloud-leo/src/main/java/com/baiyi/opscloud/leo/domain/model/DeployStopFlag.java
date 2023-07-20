package com.baiyi.opscloud.leo.domain.model;

import lombok.*;

/**
 * @Author baiyi
 * @Date 2023/2/7 10:39
 * @Version 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeployStopFlag {

    public static final DeployStopFlag NOT_STOP = DeployStopFlag.builder().build();

    @Builder.Default
    private Boolean isStop = false;

    private String username;

}
