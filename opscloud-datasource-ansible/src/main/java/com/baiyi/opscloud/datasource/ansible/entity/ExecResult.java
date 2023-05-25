package com.baiyi.opscloud.datasource.ansible.entity;

import lombok.Builder;
import lombok.Data;

import java.io.ByteArrayOutputStream;

/**
 * @Author baiyi
 * @Date 2021/8/16 10:55 上午
 * @Version 1.0
 */
@Data
@Builder
public class ExecResult {

    public static final ExecResult FAILED = ExecResult.builder()
            .exitCode(1).build();

    /**
     * 退出码 0 正常，1 异常  -1 任务执行中
     */
    @Builder.Default
    private int exitCode = -1;

    private ByteArrayOutputStream output;
    private ByteArrayOutputStream error;

    private String exceptionMsg;

    public boolean isSuccess() {
        return exitCode == 0;
    }

}
