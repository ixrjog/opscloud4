package com.baiyi.opscloud.service.business;

import com.baiyi.opscloud.algorithm.ServerPack;
import com.baiyi.opscloud.common.util.BusinessPropertyUtil;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessProperty;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import com.baiyi.opscloud.common.util.IPAddressUtil;

import jakarta.annotation.Resource;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2021/8/24 1:48 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class BizPropertyHelper {

    private final static String PUBLIC_IP = "publicIp";

    @Resource
    private BusinessPropertyService bizPropertyService;

    public static Integer getSshPort(ServerPack serverPack) {
        return Optional.ofNullable(serverPack.getProperty())
                .map(ServerProperty.Server::getMetadata)
                .map(ServerProperty.Metadata::getSshPort)
                .orElse(22);
    }

    public static String getManageIp(ServerPack serverPack) {
        return getManageIp(serverPack.getServer(), serverPack.getProperty());
    }

    public static String getManageIp(Server server, ServerProperty.Server property) {
        final String manageIp = Optional.ofNullable(property)
                .map(ServerProperty.Server::getMetadata)
                .map(ServerProperty.Metadata::getManageIp)
                .orElse(server.getPrivateIp());
        if (StringUtils.isBlank(manageIp))
            return server.getPrivateIp();
        if (PUBLIC_IP.equalsIgnoreCase(manageIp)) {
            return StringUtils.isNotBlank(server.getPublicIp()) ? server.getPublicIp() : server.getPrivateIp();
        }
        return IPAddressUtil.isIPv4LiteralAddress(manageIp) ? manageIp : server.getPrivateIp();
    }

    public static int getSshPort(ServerProperty.Server property) {
        return Optional.ofNullable(property)
                .map(ServerProperty.Server::getMetadata)
                .map(ServerProperty.Metadata::getSshPort)
                .orElse(22);
    }

    public ServerProperty.Server getBusinessProperty(Server server) {
        ServerProperty.Server serverProperty = getServerProperty(server.getId());
        ServerProperty.Server serverGroupProperty = getServerGroupProperty(server.getServerGroupId());
        if (serverProperty == null) {
            if (serverGroupProperty == null) {
                return ServerProperty.Server.builder().build();
            } else {
                return serverGroupProperty;
            }
        } else {
            if (serverGroupProperty == null) {
                return serverProperty;
            } else {
                return BusinessPropertyUtil.combineServerProperty(serverProperty, serverGroupProperty);
            }
        }
    }

    private ServerProperty.Server getServerProperty(int serverId) {
        // BusinessPropertyUtil
        BusinessProperty businessProperty = bizPropertyService.getByUniqueKey(BusinessTypeEnum.SERVER.getType(), serverId);
        if (businessProperty != null) {
            if (!StringUtils.isEmpty(businessProperty.getProperty())) {
                try {
                    return BusinessPropertyUtil.toProperty(businessProperty.getProperty(), ServerProperty.Server.class);
                } catch (JsonSyntaxException e) {
                    log.debug(e.getMessage());
                }
            }
        }
        return null;
    }

    public ServerProperty.Server getServerGroupProperty(int serverGroupId) {
        BusinessProperty businessProperty = bizPropertyService.getByUniqueKey(BusinessTypeEnum.SERVERGROUP.getType(), serverGroupId);
        if (businessProperty != null) {
            if (!StringUtils.isEmpty(businessProperty.getProperty())) {
                try {
                    return BusinessPropertyUtil.toProperty(businessProperty.getProperty(), ServerProperty.Server.class);
                } catch (JsonSyntaxException e) {
                    log.debug(e.getMessage());
                }
            }
        }
        return null;
    }

}