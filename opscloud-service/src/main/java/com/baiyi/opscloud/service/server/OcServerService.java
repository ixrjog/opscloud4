package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.OcServer;
import com.baiyi.opscloud.domain.param.server.ServerParam;

/**
 * @Author baiyi
 * @Date 2020/1/10 1:25 下午
 * @Version 1.0
 */
public interface OcServerService {

    OcServer queryOcServerByPrivateIp(String privateIp);

    int countByServerGroupId(int id);

    int countByEnvType(int envType);

    DataTable<OcServer> queryOcServerByParam(ServerParam.PageQuery pageQuery);

    DataTable<OcServer> fuzzyQueryOcServerByParam(ServerParam.PageQuery pageQuery);

    void addOcServer(OcServer ocServer);

    void updateOcServer(OcServer ocServer);

    void deleteOcServerById(int id);

}
