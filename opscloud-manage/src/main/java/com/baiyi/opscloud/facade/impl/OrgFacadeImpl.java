package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.DepartmentDecorator;
import com.baiyi.opscloud.decorator.DepartmentMemberDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.OcOrgDepartment;
import com.baiyi.opscloud.domain.generator.OcOrgDepartmentMember;
import com.baiyi.opscloud.domain.param.org.DepartmentMemberParam;
import com.baiyi.opscloud.domain.vo.org.DepartmentTreeVO;
import com.baiyi.opscloud.domain.vo.org.OcOrgDepartmentMemberVO;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;
import com.baiyi.opscloud.facade.OrgFacade;
import com.baiyi.opscloud.service.org.OcOrgDepartmentMemberService;
import com.baiyi.opscloud.service.org.OcOrgDepartmentService;
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
    private DepartmentDecorator departmentDecorator;

    @Resource
    private DepartmentMemberDecorator departmentMemberDecorator;

    public static final int ROOT_PARENT_ID = 0;

    /**
     * @param draggingParentId
     * @param dropParentId
     * @param dropType         before、after、inner
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> dropDepartmentTree(int draggingParentId, int dropParentId, String dropType) {
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
    public DepartmentTreeVO.DepartmentTree queryDepartmentTree(int parentId) {
        List<OcOrgDepartment> deptList = queryDepartmentByParentId(parentId);
        List<TreeVO.DeptTree> tree = departmentDecorator.deptListToTree(deptList);
        DepartmentTreeVO.DepartmentTree departmentTree = DepartmentTreeVO.DepartmentTree.builder()
                .parentId(ROOT_PARENT_ID)
                .tree(tree)
                .build();
        return departmentTree;
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

}
