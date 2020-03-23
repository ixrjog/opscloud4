package com.baiyi.opscloud.aliyun.ecs.instanceTemplate;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/3/20 3:03 下午
 * @Version 1.0
 */
@Data
public class Disk {

    private DiskDetail sysDisk;
    private DiskDetail dataDisk;
}
