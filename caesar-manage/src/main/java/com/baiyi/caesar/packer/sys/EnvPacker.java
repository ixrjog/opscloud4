package com.baiyi.caesar.packer.sys;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.Env;
import com.baiyi.caesar.service.sys.EnvService;
import com.baiyi.caesar.vo.env.EnvVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/25 4:43 下午
 * @Version 1.0
 */
@Component
public class EnvPacker {

    @Resource
    private EnvService envService;

    public static List<EnvVO.Env> wrapVOList(List<Env> data) {
        return data.stream().map(e -> BeanCopierUtil.copyProperties(e, EnvVO.Env.class)).collect(Collectors.toList());
    }


    public void wrap(EnvVO.IEnv iEnv) {
        Env env = envService.getByEnvType(iEnv.getEnvType());
        iEnv.setEnv(BeanCopierUtil.copyProperties(env,EnvVO.Env.class));
    }


}
