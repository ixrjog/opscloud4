package com.baiyi.opscloud.packer;

import com.baiyi.opscloud.common.annotation.BizDocWrapper;
import com.baiyi.opscloud.common.annotation.BizUserWrapper;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/5/15 20:52
 * @Version 1.0
 */
@Component
public class ServerGroupPackerDelegate {

    @BizDocWrapper
    @BizUserWrapper
    public void wrap(ServerGroupVO.ServerGroup serverGroup, IExtend iExtend) {
    }

}