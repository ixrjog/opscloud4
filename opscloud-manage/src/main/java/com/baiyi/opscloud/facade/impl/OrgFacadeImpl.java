package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.bo.OrgDepartmentMemberBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.DepartmentDecorator;
import com.baiyi.opscloud.decorator.DepartmentMemberDecorator;
import com.baiyi.opscloud.decorator.OrgDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartment;
import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartmentMember;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.org.DepartmentMemberParam;
import com.baiyi.opscloud.domain.param.org.DepartmentParam;
import com.baiyi.opscloud.domain.vo.org.DepartmentTreeVO;
import com.baiyi.opscloud.domain.vo.org.OcOrgDepartmentMemberVO;
import com.baiyi.opscloud.domain.vo.org.OcOrgDepartmentVO;
import com.baiyi.opscloud.domain.vo.org.OrgChartVO;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;
import com.baiyi.opscloud.facade.OrgFacade;
import com.baiyi.opscloud.facade.UserFacade;
import com.baiyi.opscloud.service.org.OcOrgDepartmentMemberService;
import com.baiyi.opscloud.service.org.OcOrgDepartmentService;
import com.baiyi.opscloud.service.user.OcUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/4/20 6:36 下午
 * @Version 1.0
 */
@Service
public class OrgFacadeImpl implements OrgFacade {

    @Resource
    private OcOrgDepartmentService ocOrgDepartmentService;

    @Resource
    private OcOrgDepartmentMemberService ocOrgDepartmentMemberService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private DepartmentDecorator departmentDecorator;

    @Resource
    private OrgDecorator orgDecorator;

    @Resource
    private DepartmentMemberDecorator departmentMemberDecorator;

    @Resource
    private UserFacade userFacade;

    public static final int ROOT_PARENT_ID = 0;

    /**
     * @param draggingParentId
     * @param dropParentId
     * @param dropType         before、after、inner
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> dropDepartmentTree(int draggingParentId, int dropParentId, String dropType) {
        if (draggingParentId == dropParentId)
            return BusinessWrapper.SUCCESS;
        if (draggingParentId == ROOT_PARENT_ID)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_CANNOT_DROP_ROOT);
        OcOrgDepartment draggingDept = ocOrgDepartmentService.queryOcOrgDepartmentById(draggingParentId);
        if (draggingDept == null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_NOT_EXIST);
        OcOrgDepartment dropDept = ocOrgDepartmentService.queryOcOrgDepartmentById(dropParentId);
        if (dropDept == null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_NOT_EXIST);
        switch (dropType) {
            case "before":
                return dropDepartmentBefore(draggingDept, dropDept);
            case "after":
                return dropDepartmentAfter(draggingDept, dropDept);
            case "inner":
                return dropDepartmentInner(draggingDept, dropDept);
            default:
                return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_DROP_ERROR);
        }
    }

    /**
     * 插入节点前面
     *
     * @param draggingDept
     * @param dropDept
     * @return
     */
    private BusinessWrapper<Boolean> dropDepartmentBefore(OcOrgDepartment draggingDept, OcOrgDepartment dropDept) {
        List<OcOrgDepartment> children = queryDepartmentByParentId(dropDept.getParentId());
        draggingDept.setParentId(dropDept.getParentId());
        int order = 1;
        for (int i = 0; i < children.size(); i++) {
            OcOrgDepartment c = children.get(i);
            if (c.getId() == dropDept.getId()) {
                draggingDept.setDeptOrder(order);
                ocOrgDepartmentService.updateOcOrgDepartment(draggingDept);
                order += 1;
            }
            c.setDeptOrder(order);
            ocOrgDepartmentService.updateOcOrgDepartment(c);
            order += 1;
        }
        return BusinessWrapper.SUCCESS;
    }

    /**
     * 插入节点后面
     *
     * @param draggingDept
     * @param dropDept
     * @return
     */
    private BusinessWrapper<Boolean> dropDepartmentAfter(OcOrgDepartment draggingDept, OcOrgDepartment dropDept) {
        List<OcOrgDepartment> children = queryDepartmentByParentId(dropDept.getParentId());
        draggingDept.setParentId(dropDept.getParentId());
        int order = 1;
        for (int i = 0; i < children.size(); i++) {
            OcOrgDepartment c = children.get(i);
            c.setDeptOrder(order);
            ocOrgDepartmentService.updateOcOrgDepartment(c);
            order += 1;
            if (c.getId() == dropDept.getId()) {
                draggingDept.setDeptOrder(order);
                ocOrgDepartmentService.updateOcOrgDepartment(draggingDept);
                order += 1;
            }
        }
        return BusinessWrapper.SUCCESS;
    }


    /**
     * 插入节点内部
     *
     * @param draggingDept
     * @param dropDept
     * @return
     */
    private BusinessWrapper<Boolean> dropDepartmentInner(OcOrgDepartment draggingDept, OcOrgDepartment dropDept) {
        List<OcOrgDepartment> children = queryDepartmentByParentId(dropDept.getId());
        draggingDept.setParentId(dropDept.getId());
        draggingDept.setDeptOrder(0);
        if (children != null && !children.isEmpty()) {
            for (int i = 1; i < children.size() + 1; i++) {
                children.get(i).setDeptOrder(i);
                ocOrgDepartmentService.updateOcOrgDepartment(children.get(i));
            }
        }
        ocOrgDepartmentService.updateOcOrgDepartment(draggingDept);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DepartmentTreeVO.DepartmentTree queryDepartmentTree() {
        return queryDepartmentTree(ROOT_PARENT_ID);
    }

    @Override
    public BusinessWrapper<Boolean> addDepartment(OcOrgDepartmentVO.Department department) {
        OcOrgDepartment ocOrgDepartment = BeanCopierUtils.copyProperties(department, OcOrgDepartment.class);
        ocOrgDepartment.setDeptOrder(128);
        if (department.getParentId() == null || department.getParentId() <= 0)
            ocOrgDepartment.setParentId(1);
        ocOrgDepartment.setDeptHiding(0);
        ocOrgDepartmentService.addOcOrgDepartment(ocOrgDepartment);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateDepartment(OcOrgDepartmentVO.Department department) {
        OcOrgDepartment ocOrgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(department.getId());
        ocOrgDepartment.setName(department.getName());
        ocOrgDepartment.setDeptType(department.getDeptType());
        ocOrgDepartment.setComment(department.getComment());
        ocOrgDepartmentService.updateOcOrgDepartment(ocOrgDepartment);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public OcOrgDepartmentVO.Department queryDepartmentById(int id) {
        OcOrgDepartment ocOrgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(id);
        return BeanCopierUtils.copyProperties(ocOrgDepartment, OcOrgDepartmentVO.Department.class);
    }

    @Override
    public BusinessWrapper<Boolean> delDepartmentById(int id) {
        if (id == ROOT_PARENT_ID)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_CANNOT_DELETE_ROOT);
        OcOrgDepartment ocOrgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(id);
        if (ocOrgDepartment == null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_NOT_EXIST);
        // 查询下级部门
        List<OcOrgDepartment> list = ocOrgDepartmentService.queryOcOrgDepartmentByParentId(id);
        if (list != null && !list.isEmpty())
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_SUB_DEPT_EXISTS);
        // 查询成员
        if (ocOrgDepartmentMemberService.countOcOrgDepartmentMemberByDepartmentId(id) > 0)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_MEMBER_IS_NOT_EMPTY);
        ocOrgDepartmentService.deleteOcOrgDepartmentById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<OcOrgDepartmentVO.Department> queryDepartmentPage(DepartmentParam.PageQuery pageQuery) {
        DataTable<OcOrgDepartment> table = ocOrgDepartmentService.queryOcOrgDepartmentParam(pageQuery);
        List<OcOrgDepartmentVO.Department> page = BeanCopierUtils.copyListProperties(table.getData(), OcOrgDepartmentVO.Department.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public DepartmentTreeVO.DepartmentTree queryDepartmentTree(int parentId) {
        List<OcOrgDepartment> deptList = queryDepartmentByParentId(parentId);
        List<TreeVO.DeptTree> tree = departmentDecorator.deptListToTree(deptList);
        DepartmentTreeVO.DepartmentTree departmentTree = DepartmentTreeVO.DepartmentTree.builder()
                .parentId(ROOT_PARENT_ID)
                .tree(tree)
                .build();
        return departmentTree;
    }

    @Override
    public OrgChartVO.OrgChart queryOrgChart(int parentId) {
        List<OcOrgDepartment> deptList = queryDepartmentByParentId(parentId);
        List<OrgChartVO.Children> children = orgDecorator.deptListToChart(deptList);
        OcOrgDepartmentMember ocOrgDepartmentMember = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberByLeader(parentId);
        String name = "空缺";
        if(ocOrgDepartmentMember != null){
            OcUser ocUser = ocUserService.queryOcUserById(ocOrgDepartmentMember.getUserId());
            if(ocUser != null)
                name= ocUser.getDisplayName();
        }
        OcOrgDepartment ocOrgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(parentId);
        OrgChartVO.OrgChart orgChart = OrgChartVO.OrgChart.builder()
                .id(parentId)
                .children(children)
                .name(name)
                .title(ocOrgDepartment.getName())
                .build();
        return orgChart;
    }

    private List<OcOrgDepartment> queryDepartmentByParentId(int parentId) {
        return ocOrgDepartmentService.queryOcOrgDepartmentByParentId(parentId);
    }

    @Override
    public DataTable<OcOrgDepartmentMemberVO.DepartmentMember> queryDepartmentMemberPage(DepartmentMemberParam.PageQuery pageQuery) {
        DataTable<OcOrgDepartmentMember> table = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberParam(pageQuery);
        List<OcOrgDepartmentMemberVO.DepartmentMember> page = BeanCopierUtils.copyListProperties(table.getData(), OcOrgDepartmentMemberVO.DepartmentMember.class);
        return new DataTable<>(page.stream().map(e -> departmentMemberDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addDepartmentMember(int departmentId, int userId) {
        OcOrgDepartment ocOrgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(departmentId);
        if (ocOrgDepartment == null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_NOT_EXIST);
        OcUser ocUser = ocUserService.queryOcUserById(userId);
        if (ocUser == null)
            return new BusinessWrapper<>(ErrorEnum.USER_NOT_EXIST);

        OcOrgDepartmentMember pre = ocOrgDepartmentMemberService.getOcOrgDepartmentMemberByUniqueKey(departmentId, userId);
        if (pre != null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_MEMBER_ALREADY_EXISTS);

        OrgDepartmentMemberBO orgDepartmentMemberBO = OrgDepartmentMemberBO.builder()
                .departmentId(departmentId)
                .userId(userId)
                .username(ocUser.getUsername())
                .build();

        // 添加部门成员
        ocOrgDepartmentMemberService.addOcOrgDepartmentMember(BeanCopierUtils.copyProperties(orgDepartmentMemberBO, OcOrgDepartmentMember.class));
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> joinDepartmentMember(int departmentId) {
        OcOrgDepartment ocOrgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(departmentId);
        if (ocOrgDepartment == null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_NOT_EXIST);
        if (ocOrgDepartment.getParentId() == ROOT_PARENT_ID)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_CANNOT_JOIN_ROOT);
        OcUser ocUser = userFacade.getOcUserBySession();
        if (ocUser == null)
            return new BusinessWrapper<>(ErrorEnum.USER_NOT_EXIST);
        List<OcOrgDepartmentMember> members = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberByUserId(ocUser.getId());
        for (OcOrgDepartmentMember member : members) {
            if (member.getIsLeader() == 1)
                return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_MEMBER_IS_LEADER);
            if (member.getIsApprovalAuthority() == 1)
                return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_MEMBER_IS_APPROVAL);
            ocOrgDepartmentMemberService.deleteOcOrgDepartmentMemberById(member.getId());
        }
        OrgDepartmentMemberBO orgDepartmentMemberBO = OrgDepartmentMemberBO.builder()
                .departmentId(departmentId)
                .userId(ocUser.getId())
                .username(ocUser.getUsername())
                .build();
        ocOrgDepartmentMemberService.addOcOrgDepartmentMember(BeanCopierUtils.copyProperties(orgDepartmentMemberBO, OcOrgDepartmentMember.class));
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> delDepartmentMemberById(int id) {
        ocOrgDepartmentMemberService.deleteOcOrgDepartmentMemberById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateDepartmentMemberLeader(int id) {
        OcOrgDepartmentMember member = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberById(id);
        if (member == null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_MEMBER_NOT_EXIST);
        member.setIsLeader(member.getIsLeader() == 0 ? 1 : 0);
        ocOrgDepartmentMemberService.updateOcOrgDepartmentMember(member);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateDepartmentMemberApprovalAuthority(int id) {
        OcOrgDepartmentMember member = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberById(id);
        if (member == null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_MEMBER_NOT_EXIST);
        member.setIsApprovalAuthority(member.getIsApprovalAuthority() == 0 ? 1 : 0);
        ocOrgDepartmentMemberService.updateOcOrgDepartmentMember(member);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> delOrgUserById(int userId) {
        List<OcOrgDepartmentMember> members = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberByUserId(userId);
        if (members != null)
            try {
                for (OcOrgDepartmentMember ocOrgDepartmentMember : members)
                    ocOrgDepartmentMemberService.deleteOcOrgDepartmentMemberById(ocOrgDepartmentMember.getId());
            } catch (Exception e) {
                return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_MEMBER_DELETE_ERROR);
            }
        return BusinessWrapper.SUCCESS;
    }


}
