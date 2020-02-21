package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.domain.generator.OcServer;

/**
 * @Author baiyi
 * @Date 2020/1/10 1:25 下午
 * @Version 1.0
 */
public interface OcServerService {

    OcServer queryOcServerByPrivateIp(String privateIp);

    int countByServerGroupId(int id);

}
