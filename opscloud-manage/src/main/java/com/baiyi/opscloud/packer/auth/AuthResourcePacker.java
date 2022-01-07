package com.baiyi.opscloud.packer.auth;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AuthResource;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.service.auth.AuthGroupService;
import com.baiyi.opscloud.common.util.ExtendUtil;
import com.baiyi.opscloud.domain.vo.auth.AuthResourceVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/12 3:12 下午
 * @Version 1.0
 */
@Component
public class AuthResourcePacker {

    @Resource
    private AuthGroupService authGroupService;

    public List<AuthResourceVO.Resource> wrapVOList(List<AuthResource> data) {
        return BeanCopierUtil.copyListProperties(data, AuthResourceVO.Resource.class);
    }

    public List<AuthResourceVO.Resource> wrapVOList(List<AuthResource> data, IExtend iExtend) {
        List<AuthResourceVO.Resource> resources = wrapVOList(data);
        if (!ExtendUtil.isExtend(iExtend))
            return resources;

        return resources.stream().peek(e ->
                e.setGroup(AuthGroupPacker.wrap(authGroupService.getById(e.getGroupId())))).collect(Collectors.toList());
    }
}
