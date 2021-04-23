package com.baiyi.opscloud.service.dubbo;

import com.baiyi.opscloud.domain.generator.opscloud.OcDubboMappingServer;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/12 10:42 上午
 * @Version 1.0
 */
public interface OcDubboMappingServerService {

    List<OcDubboMappingServer> queryOcDubboMappingServerByIpAndIsMapping(String ip, boolean isMapping);

    List<OcDubboMappingServer> queryOcDubboMappingServerUnmapped();

    /**
     * 查询一个已经绑定的服务器
     * @param ip
     * @return
     */
    OcDubboMappingServer queryOneBindOcDubboMappingServer(String ip);

    List<OcDubboMappingServer> queryOcDubboMappingServerByMappingId(int mappingId);

    List<OcDubboMappingServer> queryOcDubboMappingServerByTcpMappingId(int tcpMappingId);

    void addOcDubboMappingServer(OcDubboMappingServer ocDubboMappingServer);

    void updateOcDubboMappingServer(OcDubboMappingServer ocDubboMappingServer);

    OcDubboMappingServer queryOcDubboMappingServerByUniqueKey(OcDubboMappingServer ocDubboMappingServer);
}
