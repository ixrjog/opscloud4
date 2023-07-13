package com.baiyi.opscloud.facade.server.impl;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.server.ServerFacade;
import com.google.common.base.Joiner;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2023/7/13 16:31
 * @Version 1.0
 */
public class ServerTest extends BaseUnit {

    @Resource
    private ServerFacade serverFacade;

    @Test
    void test() {
        ServerParam.ServerPageQuery pageQuery = ServerParam.ServerPageQuery.builder()
                .envType(4)
                .queryName("172.30")
                .extend(false)
                .page(1)
                .length(100)
                .build();

        DataTable<ServerVO.Server> table = serverFacade.queryServerPage(pageQuery);

        for (ServerVO.Server server : table.getData()) {
           String s= Joiner.on(" ").skipNulls().join(server.getName(),server.getPrivateIp(),server.getComment());
           print(s);
        }

    }


}
