package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.org.OcOrgDepartmentMemberVO;
import com.baiyi.opscloud.service.user.OcUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/21 4:42 下午
 * @Version 1.0
 */
@Component
public class DepartmentMemberDecorator {

    @Resource
    private OcUserService ocUserService;

    public OcOrgDepartmentMemberVO.DepartmentMember decorator(OcOrgDepartmentMemberVO.DepartmentMember departmentMember) {
        OcUser ocUser = ocUserService.queryOcUserById(departmentMember.getUserId());
        if(ocUser != null){
            departmentMember.setDisplayName(ocUser.getDisplayName());
        }
        return departmentMember;
    }
}
