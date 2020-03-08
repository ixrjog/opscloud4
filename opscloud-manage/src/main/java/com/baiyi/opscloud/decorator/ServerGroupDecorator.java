package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroupType;
import com.baiyi.opscloud.domain.vo.server.OcServerGroupTypeVO;
import com.baiyi.opscloud.domain.vo.server.OcServerGroupVO;
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

    public OcServerGroupVO.ServerGroup decorator(OcServerGroupVO.ServerGroup serverGroup) {
        OcServerGroupType ocServerGroupType = ocServerGroupTypeService.queryOcServerGroupTypeByGrpType(serverGroup.getGrpType());
        OcServerGroupTypeVO.ServerGroupType serverGroupType = BeanCopierUtils.copyProperties(ocServerGroupType, OcServerGroupTypeVO.ServerGroupType.class);
        serverGroup.setServerGroupType(serverGroupType);
        return serverGroup;
    }
}
