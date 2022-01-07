package com.baiyi.opscloud.packer.auth;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AuthGroup;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.service.auth.AuthResourceService;
import com.baiyi.opscloud.common.util.ExtendUtil;
import com.baiyi.opscloud.domain.vo.auth.AuthGroupVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/12 2:34 下午
 * @Version 1.0
 */
@Component
public class AuthGroupPacker {

    @Resource
    private AuthResourceService authResourceService;

    public static List<AuthGroupVO.Group> wrapVOList(List<AuthGroup> data) {
        return BeanCopierUtil.copyListProperties(data, AuthGroupVO.Group.class);
    }

    public List<AuthGroupVO.Group> wrapVOList(List<AuthGroup> data, IExtend iExtend) {
        List<AuthGroupVO.Group> groups = wrapVOList(data);
        if (!ExtendUtil.isExtend(iExtend))
            return groups;

        return groups.stream().peek(e ->
                e.setResourceSize(authResourceService.countByGroupId(e.getId()))).collect(Collectors.toList());
    }

    public static AuthGroupVO.Group wrap(AuthGroup authGroup) {
        return BeanCopierUtil.copyProperties(authGroup, AuthGroupVO.Group.class);
    }
}
