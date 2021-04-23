package com.baiyi.opscloud.module;

import com.baiyi.opscloud.domain.generator.opscloud.OcDubboMapping;
import com.baiyi.opscloud.domain.generator.opscloud.OcDubboTcpMapping;
import com.google.common.base.Joiner;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/14 3:52 下午
 * @Version 1.0
 */
@Data
@Builder
public class DubboTcpMappingModule {

    private OcDubboTcpMapping ocDubboTcpMapping;
    private List<OcDubboMapping> ocDubboMappings;

    public String buildProperties(String tcpServer) {
        StringBuilder properties = new StringBuilder();
        properties.append("# " + ocDubboTcpMapping.getName() + "\n");
        ocDubboMappings.forEach(e -> {
            // com.ggj.item.footprint.center.api.FootprintUserStatisticsApi=dubbo://ng-tcp-test.ops.yangege.cn:30446
            String proxy = Joiner.on("").join("dubbo://", tcpServer, ":", ocDubboTcpMapping.getTcpPort() + "\n");
            properties.append(Joiner.on("=").join(e.getDubboInterface(), proxy));
        });
        return properties.toString();
    }

}
