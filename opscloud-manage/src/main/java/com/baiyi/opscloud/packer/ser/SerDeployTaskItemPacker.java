package com.baiyi.opscloud.packer.ser;

import com.baiyi.opscloud.common.util.FunctionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.ser.SerDeployVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2023/6/12 4:09 PM
 * @Since 1.0
 */

@Component
@RequiredArgsConstructor
public class SerDeployTaskItemPacker implements IWrapper<SerDeployVO.TaskItem> {

    private final UserService userService;

    @Override
    public void wrap(SerDeployVO.TaskItem vo, IExtend iExtend) {
        FunctionUtil.trueFunction(iExtend.getExtend())
                .withTrue(
                        () -> {
                            User user = userService.getByUsername(vo.getReloadUsername());
                            vo.setReloadUser(user);
                        }
                );
    }
}
