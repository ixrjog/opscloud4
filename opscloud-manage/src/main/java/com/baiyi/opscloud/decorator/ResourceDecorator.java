package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.domain.generator.OcAuthGroup;
import com.baiyi.opscloud.domain.vo.auth.OcResourceVO;
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

    public OcResourceVO.Resource decorator(OcResourceVO.Resource resource) {
        if (resource.getGroupId() == null)
            return resource;
        OcAuthGroup ocAuthGroup = ocAuthGroupService.queryOcAuthGroupById(resource.getGroupId());
        resource.setGroupCode(ocAuthGroup.getGroupCode());
        return resource;
    }
}
