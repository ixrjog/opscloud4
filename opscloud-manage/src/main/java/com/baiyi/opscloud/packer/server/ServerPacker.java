package com.baiyi.opscloud.packer.server;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.packer.ServerPackerDelegate;
import com.baiyi.opscloud.packer.IWrapper;
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
public class ServerPacker implements IWrapper<ServerVO.Server> {

    private final ServerAccountPacker accountPacker;

    private final ServerGroupPacker serverGroupPacker;

    private final BusinessPropertyPacker businessPropertyPacker;

    private final ServerPackerDelegate serverPackerDelegate;

    @Override
    public void wrap(ServerVO.Server server, IExtend iExtend) {
        // 代理
        serverPackerDelegate.wrap(server, SimpleExtend.EXTEND);
        accountPacker.wrap(server);
        serverGroupPacker.wrap(server);
        businessPropertyPacker.wrap(server);
        SimpleServerNameFacade.wrapDisplayName(server);
    }

}
