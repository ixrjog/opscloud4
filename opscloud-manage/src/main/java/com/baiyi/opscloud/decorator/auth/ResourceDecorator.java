package com.baiyi.opscloud.decorator.auth;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAuthGroup;
import com.baiyi.opscloud.domain.vo.auth.GroupVO;
import com.baiyi.opscloud.domain.vo.auth.ResourceVO;
import com.baiyi.opscloud.service.auth.OcAuthGroupService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/24 5:28 下午
 * @Version 1.0
 */
@Component("ResourceDecorator")
public class ResourceDecorator {

    @Resource
    private OcAuthGroupService ocAuthGroupService;

    public ResourceVO.Resource decorator(ResourceVO.Resource resource) {
        // 装饰资源组
        if (resource.getGroupId() == null)
            return resource;
        OcAuthGroup ocAuthGroup = ocAuthGroupService.queryOcAuthGroupById(resource.getGroupId());
        resource.setGroupCode(ocAuthGroup.getGroupCode());
        resource.setGroup( BeanCopierUtils.copyProperties(ocAuthGroup, GroupVO.Group.class));
        return resource;
    }
}
