package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.OcServerGroup;
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

    BusinessWrapper<Boolean> saveServerGroupAttribute(OcServerAttributeVO.ServerAttribute serverAttribute);

    Map<String, String> getServerGroupAttributeMap(OcServerGroup ocServerGroup);

   // Map<String, String> getServerAttributeMap(int serverId);
}
