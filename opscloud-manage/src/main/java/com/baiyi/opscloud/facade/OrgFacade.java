package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.org.DepartmentMemberParam;
import com.baiyi.opscloud.domain.param.org.DepartmentParam;
import com.baiyi.opscloud.domain.vo.org.DepartmentTreeVO;
import com.baiyi.opscloud.domain.vo.org.OcOrgDepartmentMemberVO;
import com.baiyi.opscloud.domain.vo.org.OcOrgDepartmentVO;

/**
 * @Author baiyi
 * @Date 2020/4/20 6:36 下午
 * @Version 1.0
 */
public interface OrgFacade {

    DataTable<OcOrgDepartmentVO.Department> queryDepartmentPage(DepartmentParam.PageQuery pageQuery);

    DepartmentTreeVO.DepartmentTree queryDepartmentTree(int parentId);

    DepartmentTreeVO.DepartmentTree queryDepartmentTree();

    BusinessWrapper<Boolean> addDepartment(OcOrgDepartmentVO.Department department);

    OcOrgDepartmentVO.Department queryDepartmentById(int id);

    BusinessWrapper<Boolean> delDepartmentById(int id);

    BusinessWrapper<Boolean> updateDepartment(OcOrgDepartmentVO.Department department);

    BusinessWrapper<Boolean> dropDepartmentTree(int draggingParentId, int dropParentId, String dropType);

    DataTable<OcOrgDepartmentMemberVO.DepartmentMember> queryDepartmentMemberPage(DepartmentMemberParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addDepartmentMember(int departmentId, int userId);

    BusinessWrapper<Boolean> joinDepartmentMember(int departmentId);

    BusinessWrapper<Boolean> delDepartmentMemberById(int id);

    BusinessWrapper<Boolean> updateDepartmentMemberLeader(int id);

    BusinessWrapper<Boolean> updateDepartmentMemberApprovalAuthority(int id);

    /**
     * 删除用户的所有组织架构信息
     * @param userId
     * @return
     */
    BusinessWrapper<Boolean> delOrgUserById(int userId);

}
