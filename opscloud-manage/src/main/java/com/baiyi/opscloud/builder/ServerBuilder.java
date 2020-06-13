package com.baiyi.opscloud.builder;

import com.baiyi.opscloud.bo.ServerBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.vo.server.ServerVO;

/**
 * @Author baiyi
 * @Date 2020/4/1 11:07 上午
 * @Version 1.0
 */
public class ServerBuilder {

    public static OcServer build(ServerVO.Server server) {
        ServerBO serverBO = ServerBO.builder()
                .name(server.getName())
                .serialNumber(server.getSerialNumber())
                .envType(server.getEnvType())
                .loginUser(server.getLoginUser())
                .loginType(server.getLoginType())
                .serverGroupId(server.getServerGroupId())
                .publicIp(server.getPublicIp())
                .privateIp(server.getPrivateIp())
                .serverType(server.getServerType())
                .area(server.getArea())
                .comment(server.getComment())
                .build();
        return covert(serverBO);
    }

    private static OcServer covert( ServerBO serverBO) {
        return BeanCopierUtils.copyProperties(serverBO, OcServer.class);
    }
}
