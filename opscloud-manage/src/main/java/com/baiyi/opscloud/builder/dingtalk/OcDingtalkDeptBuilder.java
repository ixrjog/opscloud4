package com.baiyi.opscloud.builder.dingtalk;

import com.baiyi.opscloud.domain.generator.opscloud.OcDingtalkDept;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 3:26 下午
 * @Since 1.0
 */
public class OcDingtalkDeptBuilder {

    public static OcDingtalkDept build(OapiV2DepartmentListsubResponse.DeptBaseResponse deptBaseResponse, String uid) {
        OcDingtalkDept ocDingtalkDept = new OcDingtalkDept();
        ocDingtalkDept.setDingtalkUid(uid);
        ocDingtalkDept.setDeptId(deptBaseResponse.getDeptId());
        ocDingtalkDept.setDeptName(deptBaseResponse.getName());
        ocDingtalkDept.setParentId(deptBaseResponse.getParentId());
        ocDingtalkDept.setCreateDeptGroup(deptBaseResponse.getCreateDeptGroup());
        ocDingtalkDept.setAutoAddUser(deptBaseResponse.getAutoAddUser());
        return ocDingtalkDept;
    }
}
