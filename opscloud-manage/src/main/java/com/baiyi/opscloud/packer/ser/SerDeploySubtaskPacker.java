package com.baiyi.opscloud.packer.ser;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.common.annotation.RuntimeWrapper;
import com.baiyi.opscloud.common.holder.WorkOrderSerDeployHolder;
import com.baiyi.opscloud.common.util.FunctionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.SerDeploySubtaskCallback;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.ser.SerDeployVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.ser.SerDeploySubtaskCallbackService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.base.Global.ENV_PROD;

/**
 * @Author 修远
 * @Date 2023/6/12 4:10 PM
 * @Since 1.0
 */

@Component
@RequiredArgsConstructor
public class SerDeploySubtaskPacker implements IWrapper<SerDeployVO.SubTask> {

    private final EnvService envService;

    private final UserService userService;

    private final SerDeploySubtaskCallbackService serDeploySubtaskCallbackService;

    private final WorkOrderSerDeployHolder workOrderSerDeployHolder;

    @Override
    @AgoWrapper
    @RuntimeWrapper
    public void wrap(SerDeployVO.SubTask vo, IExtend iExtend) {
        FunctionUtil.trueFunction(iExtend.getExtend())
                .withTrue(
                        () -> {
                            Env env = envService.getByEnvType(vo.getEnvType());
                            vo.setEnv(env);
                            if (ENV_PROD.equals(env.getEnvName())) {
                                vo.setTicketFlag(workOrderSerDeployHolder.hasKey(vo.getSerDeployTaskId()));
                            }
                            if (StringUtils.isNotBlank(vo.getDeployUsername())) {
                                User user = userService.getByUsername(vo.getDeployUsername());
                                vo.setDeployUser(user);
                            }
                            List<String> callbackContents = serDeploySubtaskCallbackService.listBySerDeploySubtaskId(vo.getId()).stream()
                                    .map(SerDeploySubtaskCallback::getCallbackContent).toList();
                            vo.setCallbackContent(Joiner.on("\n").join(callbackContents));
                        }
                );
    }
}
