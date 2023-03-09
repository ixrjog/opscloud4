package com.baiyi.opscloud.packer.auth;

import com.baiyi.opscloud.common.util.ExtendUtil;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.auth.AuthResourceVO;
import com.baiyi.opscloud.packer.IWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/12 3:12 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AuthResourcePacker implements IWrapper<AuthResourceVO.Resource> {

    private final AuthGroupPacker authGroupPacker;

    @Override
    public void wrap(AuthResourceVO.Resource resource, IExtend iExtend) {
        if (ExtendUtil.isExtend(iExtend)) {
            authGroupPacker.wrap(resource);
        }
    }

}
