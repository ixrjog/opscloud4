package com.baiyi.opscloud.packer.server;

import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.packer.business.BusinessPropertyPacker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/25 3:41 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ServerPacker {

    private final ServerAccountPacker accountPacker;

    private final ServerGroupPacker serverGroupPacker;

    private final BusinessPropertyPacker businessPropertyPacker;

    @EnvWrapper
    @TagsWrapper
    public void wrap(ServerVO.Server server, IExtend iExtend) {
        accountPacker.wrap(server);
        serverGroupPacker.wrap(server);
        businessPropertyPacker.wrap(server);
        SimpleServerNameFacade.wrapDisplayName(server);
    }

    @EnvWrapper(extend = true)
    public void wrapVO(ServerVO.Server server) {
        SimpleServerNameFacade.wrapDisplayName(server);
    }

}
