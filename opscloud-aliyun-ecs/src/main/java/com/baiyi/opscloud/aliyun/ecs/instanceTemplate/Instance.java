package com.baiyi.opscloud.aliyun.ecs.instanceTemplate;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/3/20 3:00 下午
 * @Version 1.0
 */
@Data
public class Instance {
    private String typeFamily;
    private String typeId;
    private Integer cpuCoreCount;
    private Float memorySize;
}
