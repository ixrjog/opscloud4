package com.baiyi.opscloud.service.business;

import com.baiyi.opscloud.algorithm.ServerPack;
import com.baiyi.opscloud.common.util.BusinessPropertyUtil;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessProperty;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2021/8/24 1:48 下午
 * @Version 1.0
 */
@Component
public class BusinessPropertyHelper {

    @Resource
    private BusinessPropertyService businessPropertyService;

    public static Integer getSshPort(ServerPack serverPack){
        return Optional.ofNullable(serverPack.getProperty())
                .map(ServerProperty.Server::getMetadata)
                .map(ServerProperty.Metadata::getSshPort)
                .orElse(22);
    }

    public static String getManageIp(ServerPack serverPack){
        return getManageIp(serverPack.getServer(),serverPack.getProperty());
    }

    public static String getManageIp(Server server, ServerProperty.Server property) {
        String manageIp = Optional.ofNullable(property)
                .map(ServerProperty.Server::getMetadata)
                .map(ServerProperty.Metadata::getManageIp)
                .orElse(server.getPrivateIp());
        return StringUtils.isEmpty(manageIp) ? server.getPrivateIp() : manageIp;
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
        BusinessProperty businessProperty = businessPropertyService.getByUniqueKey(BusinessTypeEnum.SERVER.getType(), serverId);
        if (businessProperty != null) {
            if (!StringUtils.isEmpty(businessProperty.getProperty())) {
                try {
                    return BusinessPropertyUtil.toProperty(businessProperty.getProperty(), ServerProperty.Server.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public ServerProperty.Server getServerGroupProperty(int serverGroupId) {
        BusinessProperty businessProperty = businessPropertyService.getByUniqueKey(BusinessTypeEnum.SERVERGROUP.getType(), serverGroupId);
        if (businessProperty != null) {
            if (!StringUtils.isEmpty(businessProperty.getProperty())) {
                try {
                    return BusinessPropertyUtil.toProperty(businessProperty.getProperty(), ServerProperty.Server.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
