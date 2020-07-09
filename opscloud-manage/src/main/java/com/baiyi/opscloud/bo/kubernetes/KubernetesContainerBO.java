package com.baiyi.opscloud.bo.kubernetes;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/7/4 4:18 下午
 * @Version 1.0
 */
@Data
@Builder
public class KubernetesContainerBO {

    private String image;
    private String imageID;
    private String name;
    private Boolean ready;
    private Boolean started;
    private String containerID;
    private String id;
}
