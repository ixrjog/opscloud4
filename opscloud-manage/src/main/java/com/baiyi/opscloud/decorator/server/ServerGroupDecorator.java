package com.baiyi.opscloud.decorator.server;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroupType;
import com.baiyi.opscloud.domain.vo.server.ServerGroupTypeVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.service.server.OcServerGroupTypeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/22 10:47 上午
 * @Version 1.0
 */
@Component("ServerGroupDecorator")
public class ServerGroupDecorator {

    @Resource
    private OcServerGroupTypeService ocServerGroupTypeService;

    public ServerGroupVO.ServerGroup decorator(ServerGroupVO.ServerGroup serverGroup) {
        OcServerGroupType ocServerGroupType = ocServerGroupTypeService.queryOcServerGroupTypeByGrpType(serverGroup.getGrpType());
        ServerGroupTypeVO.ServerGroupType serverGroupType = BeanCopierUtils.copyProperties(ocServerGroupType, ServerGroupTypeVO.ServerGroupType.class);
        serverGroup.setServerGroupType(serverGroupType);
        return serverGroup;
    }
}
