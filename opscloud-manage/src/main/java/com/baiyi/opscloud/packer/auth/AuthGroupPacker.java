package com.baiyi.opscloud.packer.auth;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.ExtendUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AuthGroup;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.auth.AuthGroupVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.auth.AuthGroupService;
import com.baiyi.opscloud.service.auth.AuthResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/12 2:34 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AuthGroupPacker implements IWrapper<AuthGroupVO.Group> {

    private final AuthGroupService authGroupService;

    private final AuthResourceService authResourceService;

    @Override
    public void wrap(AuthGroupVO.Group group, IExtend iExtend) {
        if (!ExtendUtil.isExtend(iExtend)) {
            return;
        }
        group.setResourceSize(authResourceService.countByGroupId(group.getId()));
    }

    public void wrap(AuthGroupVO.IAuthGroup iAuthGroup) {
        if (IdUtil.isEmpty(iAuthGroup.getGroupId())) {
            return;
        }
        AuthGroup authGroup = authGroupService.getById(iAuthGroup.getGroupId());
        if (authGroup == null) {
            return;
        }
        iAuthGroup.setGroup(BeanCopierUtil.copyProperties(authGroup, AuthGroupVO.Group.class));
    }

}
