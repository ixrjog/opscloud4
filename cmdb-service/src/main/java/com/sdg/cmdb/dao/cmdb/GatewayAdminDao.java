package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.gatewayAdmin.GatewayAdminDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GatewayAdminDao {

    long getGatewayAdminSize(@Param("serverGroupName") String serverGroupName,
                             @Param("appName") String appName);


    List<GatewayAdminDO> getGatewayAdminPage(@Param("serverGroupName") String serverGroupName,
                                             @Param("appName") String appName,
                                             @Param("pageStart") long pageStart,
                                             @Param("length") int length);

    List<GatewayAdminDO> getGatewayAdminAll();

    int addGatewayAdmin(GatewayAdminDO gatewayAdminDO);

    int updateGatewayAdmin(GatewayAdminDO gatewayAdminDO);

    int delGatewayAdmin(@Param("id") long id);

    GatewayAdminDO getGatewayAdminByAppName(@Param("appName") String appName);

    GatewayAdminDO getGatewayAdmin(@Param("id") long id);
}
