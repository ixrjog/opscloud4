package com.baiyi.opscloud.packer;

import com.baiyi.opscloud.common.annotation.BizDocWrapper;
import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/2/17 5:23 PM
 * @Version 1.0
 */
@Component
public class ServerPackerDelegate {

    @EnvWrapper
    @TagsWrapper
    @BizDocWrapper
    public void wrap(ServerVO.Server server, IExtend iExtend) {
    }

}
