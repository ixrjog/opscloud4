package com.baiyi.opscloud.bo.kubernetes;

import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesPodVO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/4 4:18 下午
 * @Version 1.0
 */
@Data
@Builder
public class KubernetesPodBO {

    private String instanceName;
    private String name;
    private List<KubernetesPodVO.Container> containers;
    private String hostIP;
    private String podIP;
    private String phase;
}
