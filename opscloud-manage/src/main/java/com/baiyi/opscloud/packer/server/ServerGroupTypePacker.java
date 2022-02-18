package com.baiyi.opscloud.packer.server;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroupType;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.server.ServerGroupTypeVO;
import com.baiyi.opscloud.packer.base.IWrapper;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.server.ServerGroupTypeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/24 11:10 上午
 * @Version 1.0
 */
@Component
public class ServerGroupTypePacker implements IWrapper<ServerGroupTypeVO.ServerGroupType> {

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private ServerGroupTypeService serverGroupTypeService;

    public void wrap(ServerGroupTypeVO.IServerGroupType iServerGroupType) {
        ServerGroupType serverGroupType = serverGroupTypeService.getById(iServerGroupType.getServerGroupTypeId());
        iServerGroupType.setServerGroupType(BeanCopierUtil.copyProperties(serverGroupType, ServerGroupTypeVO.ServerGroupType.class));
    }

    @Override
    public void wrap(ServerGroupTypeVO.ServerGroupType serverGroupType, IExtend iExtend) {
        serverGroupType.setServerGroupSize(serverGroupService.countByServerGroupTypeId(serverGroupType.getId()));
    }

}
