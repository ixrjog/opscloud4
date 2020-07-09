package com.baiyi.opscloud.decorator.department;

import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartment;
import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartmentMember;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.org.OrgChartVO;
import com.baiyi.opscloud.facade.OrgFacade;
import com.baiyi.opscloud.service.org.OcOrgDepartmentMemberService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
        if (CollectionUtils.isEmpty(deptList))
            return null;
        List<OrgChartVO.Children> childrens = Lists.newArrayList();
        deptList.forEach(e -> {
            OrgChartVO.Children children = OrgChartVO.Children.builder()
                    .id(e.getId())
                    .name(getName(e))
                    .title(e.getName())
                    .build();
            invokeChildren(children);
            childrens.add(children);
        });
        return childrens;
    }

    private String getName(OcOrgDepartment ocOrgDepartment){
        OcOrgDepartmentMember ocOrgDepartmentMember = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberByLeader(ocOrgDepartment.getId());
        if (ocOrgDepartmentMember != null) {
            OcUser ocUser = ocUserService.queryOcUserById(ocOrgDepartmentMember.getUserId());
            if (ocUser != null)
                return ocUser.getDisplayName();
        }
        return "空缺";
    }

    /**
     * 组织架构递归算法
     *
     * @param children
     */
    private void invokeChildren(OrgChartVO.Children children) {
        OrgChartVO.OrgChart orgChart = orgFacade.queryOrgChart(children.getId());
        if (orgChart == null)
            return;
        if (orgChart.getChildren() != null) {
            orgChart.getChildren().forEach(this::invokeChildren);
            children.setChildren(orgChart.getChildren());
        }
    }
}
