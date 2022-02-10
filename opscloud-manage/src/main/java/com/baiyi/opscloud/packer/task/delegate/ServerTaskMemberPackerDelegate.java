package com.baiyi.opscloud.packer.task.delegate;

import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.domain.vo.task.ServerTaskMemberVO;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/2/9 5:25 PM
 * @Version 1.0
 */
@Component
public class ServerTaskMemberPackerDelegate {

    @EnvWrapper(extend = true)
    public void wrap(ServerTaskMemberVO.Member member) {
    }

}
