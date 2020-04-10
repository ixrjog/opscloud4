package com.baiyi.opscloud.ansible.bo;

import lombok.Builder;
import lombok.Data;

import java.io.ByteArrayOutputStream;

/**
 * @Author baiyi
 * @Date 2020/4/6 6:39 下午
 * @Version 1.0
 */
@Data
@Builder
public class TaskResult {

    public static final TaskResult FAILED = TaskResult.builder()
            .exitValue(1).build();


    /**
     * 退出码 0 正常，1 异常  -1 任务执行中
     */
    @Builder.Default
    private int exitValue = -1;

    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errorStream;

    private String exceptionMsg;

    public boolean isSuccess() {
        return exitValue == 0;
    }

}
