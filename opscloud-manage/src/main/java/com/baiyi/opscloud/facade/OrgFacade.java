package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.org.DepartmentMemberParam;
import com.baiyi.opscloud.domain.vo.org.DepartmentTreeVO;
import com.baiyi.opscloud.domain.vo.org.OcOrgDepartmentMemberVO;

/**
 * @Author baiyi
 * @Date 2020/4/20 6:36 下午
 * @Version 1.0
 */
public interface OrgFacade {

    DepartmentTreeVO.DepartmentTree queryDepartmentTree(int parentId);

    DepartmentTreeVO.DepartmentTree queryDepartmentTree();

    BusinessWrapper<Boolean> dropDepartmentTree(int draggingParentId, int dropParentId, String dropType);

    DataTable<OcOrgDepartmentMemberVO.DepartmentMember> queryDepartmentMemberPage(DepartmentMemberParam.PageQuery pageQuery);
}
