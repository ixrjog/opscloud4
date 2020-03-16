package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerAttribute;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.vo.server.OcServerAttributeVO;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/3/7 10:28 上午
 * @Version 1.0
 */
public interface ServerAttributeFacade {

    List<OcServerAttributeVO.ServerAttribute> queryServerGroupAttribute(OcServerGroup ocServerGroup);

    List<OcServerAttributeVO.ServerAttribute> queryServerAttribute(OcServer ocServer);

    List<OcServerAttribute> queryServerAttributeById(int serverId);

   void deleteServerAttributeByList(List<OcServerAttribute> serverAttributeList);

    BusinessWrapper<Boolean> saveServerAttribute(OcServerAttributeVO.ServerAttribute serverAttribute);

    Map<String, String> getServerGroupAttributeMap(OcServerGroup ocServerGroup);

    Map<String, String> getServerAttributeMap(OcServer ocServer);

    String getManageIp(OcServer ocServer);

    // Map<String, String> getServerAttributeMap(int serverId);
}
