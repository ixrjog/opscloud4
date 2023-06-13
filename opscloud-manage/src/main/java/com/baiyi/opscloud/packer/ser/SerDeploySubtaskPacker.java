package com.baiyi.opscloud.packer.ser;

import com.baiyi.opscloud.common.util.FunctionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.ser.SerDeployVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.sys.EnvService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2023/6/12 4:10 PM
 * @Since 1.0
 */

@Component
@RequiredArgsConstructor
public class SerDeploySubtaskPacker implements IWrapper<SerDeployVO.SubTask> {

    private final EnvService envService;

    @Override
    public void wrap(SerDeployVO.SubTask vo, IExtend iExtend) {
        FunctionUtil.trueFunction(iExtend.getExtend())
                .trueHandle(
                        () -> {
                            Env env = envService.getByEnvType(vo.getEnvType());
                            vo.setEnv(env);
                        }
                );
    }
}
