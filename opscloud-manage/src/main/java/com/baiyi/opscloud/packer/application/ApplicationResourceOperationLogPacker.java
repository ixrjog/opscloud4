package com.baiyi.opscloud.packer.application;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceOperationLogVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/7/5 16:27
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ApplicationResourceOperationLogPacker implements IWrapper<ApplicationResourceOperationLogVO.OperationLog> {

    private final UserService userService;

    @Override
    public void wrap(ApplicationResourceOperationLogVO.OperationLog operationLog, IExtend iExtend) {
    }

    @Override
    @AgoWrapper(extend = true)
    public void wrap(ApplicationResourceOperationLogVO.OperationLog operationLog){
        User user = userService.getByUsername(operationLog.getUsername());
        if (user == null) return;
        operationLog.setUser(
                UserVO.User.builder()
                        .username(user.getUsername())
                        .displayName(user.getDisplayName())
                        .name(user.getName())
                        .build()
        );
    }

}
