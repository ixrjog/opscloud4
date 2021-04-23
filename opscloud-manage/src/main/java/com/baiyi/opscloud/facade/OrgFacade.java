package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartment;
import com.baiyi.opscloud.domain.param.org.DepartmentMemberParam;
import com.baiyi.opscloud.domain.param.org.DepartmentParam;
import com.baiyi.opscloud.domain.vo.org.*;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/20 6:36 下午
 * @Version 1.0
 */
public interface OrgFacade {

    DataTable<OrgDepartmentVO.Department> queryDepartmentPage(DepartmentParam.PageQuery pageQuery);

    DepartmentTreeVO.DepartmentTree queryDepartmentTree(int parentId);

    BusinessWrapper<List<TreeVO.Tree>> queryDepartmentTreeV2();

    BusinessWrapper<List<TreeVO.Tree>> refreshDepartmentTreeV2();

    /**
     * 查询组织架构拓扑
     *
     * @param parentId 当值为 -1 则按setting配置查询
     * @return
     */
    OrgChartVO.OrgChart queryOrgChart(int parentId);

    DepartmentTreeVO.DepartmentTree queryDepartmentTree();

    BusinessWrapper<Boolean> addDepartment(OrgDepartmentVO.Department department);

    OrgDepartmentVO.Department queryDepartmentById(int id);

    BusinessWrapper<Boolean> delDepartmentById(int id);

    BusinessWrapper<Boolean> updateDepartment(OrgDepartmentVO.Department department);

    BusinessWrapper<Boolean> dropDepartmentTree(int draggingParentId, int dropParentId, String dropType);

    DataTable<OrgDepartmentMemberVO.DepartmentMember> queryDepartmentMemberPage(DepartmentMemberParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addDepartmentMember(int departmentId, int userId);

    BusinessWrapper<Boolean> joinDepartmentMember(int departmentId);

    BusinessWrapper<Boolean> delDepartmentMemberById(int id);

    BusinessWrapper<Boolean> updateDepartmentMemberLeader(int id);

    BusinessWrapper<Boolean> updateDepartmentMemberApprovalAuthority(int id);

    /**
     * 删除用户的所有组织架构信息
     *
     * @param userId
     * @return
     */
    BusinessWrapper<Boolean> delOrgUserById(int userId);

    BusinessWrapper<Boolean> checkUserInTheDepartment();

    BusinessWrapper<Boolean> checkUserInTheDepartment(String username);

    BusinessWrapper<OrgApprovalVO.OrgApproval> queryOrgApprovalByName(String userName);

    BusinessWrapper<Map<String, List<OcOrgDepartment>>> queryOrgByUser(Integer userId);

    BusinessWrapper<List<OrgDepartmentMemberVO.DepartmentMember>> queryOrgByUserV2(Integer userId);

    BusinessWrapper<Map<String, List<OcOrgDepartment>>> queryOrgByUsername(String username);

    DataTable<OrgDepartmentVO.Department> queryFirstLevelDepartmentPage(DepartmentParam.PageQuery pageQuery);

    BusinessWrapper<List<OcOrgDepartment>> queryDeptPath(Integer departmentId);
}
