package com.baiyi.opscloud.decorator.department;

import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartment;
import com.baiyi.opscloud.domain.vo.org.DepartmentTreeVO;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;
import com.baiyi.opscloud.facade.OrgFacade;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/21 4:47 下午
 * @Version 1.0
 */
@Component
public class DepartmentDecorator {

    @Resource
    private OrgFacade orgFacade;

    public List<TreeVO.DeptTree> deptListToTree(List<OcOrgDepartment> deptList) {
        if (deptList == null || deptList.isEmpty())
            return null;
        List<TreeVO.DeptTree> treeList = Lists.newArrayList();
        deptList.forEach(e -> {
            TreeVO.DeptTree tree = TreeVO.DeptTree.builder()
                    .id(e.getId())
                    .label(e.getName())
                    .build();
            invokeChildren(tree);
            treeList.add(tree);
        });
        return treeList;
    }

    /**
     * 组织架构递归算法
     *
     * @param tree
     */
    private void invokeChildren(TreeVO.DeptTree tree) {
        DepartmentTreeVO.DepartmentTree departmentTree = orgFacade.queryDepartmentTree(tree.getId());
        if (departmentTree == null)
            return;
        if (departmentTree.getTree() != null) {
            departmentTree.getTree().forEach(this::invokeChildren);
            tree.setChildren(departmentTree.getTree());
        }
    }


}
