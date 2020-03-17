package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAuthGroup;
import com.baiyi.opscloud.domain.vo.auth.OcGroupVO;
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
        // 装饰资源组
        if (resource.getGroupId() == null)
            return resource;
        OcAuthGroup ocAuthGroup = ocAuthGroupService.queryOcAuthGroupById(resource.getGroupId());
        resource.setGroupCode(ocAuthGroup.getGroupCode());
        resource.setGroup( BeanCopierUtils.copyProperties(ocAuthGroup, OcGroupVO.Group.class));
        return resource;
    }
}
