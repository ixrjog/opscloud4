package com.baiyi.opscloud.datasource.jenkins.message;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2022/8/1 16:02
 * @Version 1.0
 */
@Data
public class SimpleMessage  {

    private Integer instanceId;
    private String instanceUuid;

}