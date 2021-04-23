package com.baiyi.opscloud.decorator.department;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartment;
import com.baiyi.opscloud.domain.vo.org.DepartmentTreeVO;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;
import com.baiyi.opscloud.facade.OrgFacade;
import com.baiyi.opscloud.service.org.OcOrgDepartmentService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @Author baiyi
 * @Date 2020/4/21 4:47 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class DepartmentDecorator {

    private static final Integer ROOT_DEPT_PARENT_ID = 0;

    private static final Integer ROOT_DEPT_ID = 1;

    private static Map<String, List<OcOrgDepartment>> deptMap = Maps.newHashMap();

    @Resource
    private OrgFacade orgFacade;

    @Resource
    private OcOrgDepartmentService ocOrgDepartmentService;

    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_COMMON_BY_DAY, key = "'decoratorDeptListToTree_' + #parentId", unless = "#result == null")
    public List<TreeVO.DeptTree> deptListToTree(int parentId) {
        List<OcOrgDepartment> deptList = ocOrgDepartmentService.queryOcOrgDepartmentByParentId(parentId);
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

    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_COMMON_BY_DAY, key = "'departmentTreeDecorator'", beforeInvocation = true)
    public void evictPreview() {
        deptMap.clear();
        List<OcOrgDepartment> departmentList = ocOrgDepartmentService.queryOcOrgDepartmentAll();
        departmentList.forEach(ocOrgDepartment -> evictDeptListToTreeCache(ocOrgDepartment.getParentId()));
    }

    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_COMMON_BY_DAY, key = "'decoratorDeptListToTree_' + #parentId", beforeInvocation = true)
    public void evictDeptListToTreeCache(int parentId) {
    }

    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_COMMON_BY_DAY, key = "'departmentTreeDecorator'")
    public List<TreeVO.Tree> decoratorTreeVO() {
        OcOrgDepartment rootDept = ocOrgDepartmentService.queryOcOrgDepartmentByParentId(ROOT_DEPT_PARENT_ID).get(0);
        return getChildDeptTree(rootDept);
    }

    // 递归计算以入参为根节点的子节点
    private List<TreeVO.Tree> getChildDeptTree(OcOrgDepartment ocOrgDepartment) {
        List<OcOrgDepartment> childDeptList = ocOrgDepartmentService.queryOcOrgDepartmentByParentId(ocOrgDepartment.getId());
        if (CollectionUtils.isEmpty(childDeptList))
            return Collections.emptyList();
        List<TreeVO.Tree> childDeptTreeList = Lists.newArrayListWithCapacity(childDeptList.size());
        childDeptList.forEach(childDept -> {
            List<TreeVO.Tree> list = getChildDeptTree(childDept);
            TreeVO.Tree childDeptTree = TreeVO.Tree.builder()
                    .label(childDept.getName())
                    .value(childDept.getId())
                    .build();
            if (!CollectionUtils.isEmpty(list))
                childDeptTree.setChildren(list);
            childDeptTreeList.add(childDeptTree);
        });
        return childDeptTreeList;
    }

    public Map<String, List<OcOrgDepartment>> getDeptMap(List<OcOrgDepartment> ocOrgDepartmentList) {
        Map<String, List<OcOrgDepartment>> map = Maps.newHashMapWithExpectedSize(ocOrgDepartmentList.size());
        ocOrgDepartmentList.forEach(department -> {
            if (!deptMap.containsKey(department.getName()))
                deptMap.put(department.getName(), getDeptList(department));
            map.put(department.getName(), deptMap.get(department.getName()));
        });
        return map;
    }

    public List<OcOrgDepartment> getDeptPath(Integer departmentId) {
        OcOrgDepartment orgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(departmentId);
        if (!deptMap.containsKey(orgDepartment.getName()))
            deptMap.put(orgDepartment.getName(), getDeptList(orgDepartment));
        return deptMap.get(orgDepartment.getName());
    }

    private Stack<OcOrgDepartment> getDeptList(OcOrgDepartment ocOrgDepartment) {
        Stack<OcOrgDepartment> stack = new Stack<>();
        OcOrgDepartment dept = ocOrgDepartment;
        while (dept != null) {
            stack.push(dept);
            dept = getParentDept(dept);
        }
        Collections.reverse(stack);
        return stack;
    }

    private OcOrgDepartment getParentDept(OcOrgDepartment ocOrgDepartment) {
        if (ocOrgDepartment.getParentId().equals(ROOT_DEPT_ID)) {
            return null;
        }
        return ocOrgDepartmentService.queryOcOrgDepartmentById(ocOrgDepartment.getParentId());
    }

}
