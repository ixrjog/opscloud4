package com.baiyi.opscloud.datasource.ansible.model;

import lombok.Builder;
import lombok.Data;

import java.io.ByteArrayOutputStream;

/**
 * @Author baiyi
 * @Date 2021/8/31 10:33 上午
 * @Version 1.0
 */
@Data
@Builder
public class AnsibleExecuteResult {

    public static final AnsibleExecuteResult FAILED = AnsibleExecuteResult.builder()
            .exitValue(1).build();

    @Builder.Default
    private int exitValue = -1;
    private Exception exception;


    private ByteArrayOutputStream output;
    private ByteArrayOutputStream error;


}
