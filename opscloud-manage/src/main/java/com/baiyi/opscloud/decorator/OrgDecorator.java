package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartment;
import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartmentMember;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.org.OrgChartVO;
import com.baiyi.opscloud.facade.OrgFacade;
import com.baiyi.opscloud.service.org.OcOrgDepartmentMemberService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/8 5:03 下午
 * @Version 1.0
 */
@Component
public class OrgDecorator {

    @Resource
    private OrgFacade orgFacade;

    @Resource
    private OcOrgDepartmentMemberService ocOrgDepartmentMemberService;


    @Resource
    private OcUserService ocUserService;

    public List<OrgChartVO.Children> deptListToChart(List<OcOrgDepartment> deptList) {
        if (deptList == null || deptList.isEmpty())
            return null;
        List<OrgChartVO.Children> Childrens = Lists.newArrayList();
        for (OcOrgDepartment ocOrgDepartment : deptList) {
            String name = "空缺";
            OcOrgDepartmentMember ocOrgDepartmentMember = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberByLeader(ocOrgDepartment.getId());
            if(ocOrgDepartmentMember != null){
              OcUser ocUser = ocUserService.queryOcUserById(ocOrgDepartmentMember.getUserId());
              if(ocUser != null)
                  name= ocUser.getDisplayName();
            }
            OrgChartVO.Children children = OrgChartVO.Children.builder()
                    .id(ocOrgDepartment.getId())
                    .name(name)
                    .title(ocOrgDepartment.getName())
                    .build();

            invokeChildren(children);
            Childrens.add(children);
        }
        return Childrens;
    }

    /**
     * 组织架构递归算法
     *
     * @param children
     */
    private void invokeChildren(OrgChartVO.Children children ) {
        OrgChartVO.OrgChart orgChart = orgFacade.queryOrgChart(children.getId());
        if (orgChart == null)
            return;
        if (orgChart.getChildren() != null) {
            for (OrgChartVO.Children c : orgChart.getChildren())
                invokeChildren(c);
            children.setChildren(orgChart.getChildren());
        }
    }
}
