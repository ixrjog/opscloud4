package com.baiyi.opscloud.decorator.aliyun.slb;

import com.aliyuncs.slb.model.v20140515.DescribeHealthStatusResponse;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;
import com.baiyi.opscloud.facade.ServerBaseFacade;
import com.baiyi.opscloud.service.server.OcServerService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 5:17 下午
 * @Since 1.0
 */

@Component
public class AliyunSLBBackendServerDecorator {

    // privateIp
    private Map<String, OcServer> serverMap;

    @Resource
    private OcServerService ocServerService;

    public AliyunSLBVO.BackendServer decoratorVO(DescribeHealthStatusResponse.BackendServer backendServer) {
        if (CollectionUtils.isEmpty(serverMap))
            serverMap = Maps.newHashMap();
        AliyunSLBVO.BackendServer backendServerVO = BeanCopierUtils.copyProperties(backendServer, AliyunSLBVO.BackendServer.class);
        backendServerVO.setProtocol(backendServer.getBizProtocol());
        OcServer ocServer;
        if (serverMap.containsKey(backendServer.getServerIp())) {
            ocServer = serverMap.get(backendServer.getServerIp());
        } else {
            ocServer = ocServerService.queryOcServerByPrivateIp(backendServer.getServerIp());
            if (ocServer != null)
                serverMap.put(backendServer.getServerIp(), ocServer);
        }
        String serverName = ocServer == null ? "unknown" : ServerBaseFacade.acqServerName(ocServer);
        Integer serverGroupId = ocServer == null ? -1 : ocServer.getServerGroupId();
        backendServerVO.setServerName(serverName);
        backendServerVO.setServerGroupId(serverGroupId);
//        backendServerVO.setOpscloudServerBO(opscloudServerBO);
        return backendServerVO;
    }

    public List<AliyunSLBVO.BackendServer> decoratorVOList(List<DescribeHealthStatusResponse.BackendServer> backendServerList) {
        if (CollectionUtils.isEmpty(backendServerList))
            return Collections.emptyList();
        serverMap = Maps.newHashMap();
        List<AliyunSLBVO.BackendServer> backendServerVOList = Lists.newArrayListWithCapacity(backendServerList.size());
        backendServerList.forEach(backendServer -> backendServerVOList.add(decoratorVO(backendServer)));
        return backendServerVOList;
    }
}
