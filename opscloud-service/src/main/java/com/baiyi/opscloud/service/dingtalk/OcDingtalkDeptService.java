package com.baiyi.opscloud.service.dingtalk;

import com.baiyi.opscloud.domain.generator.opscloud.OcDingtalkDept;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 3:07 下午
 * @Since 1.0
 */
public interface OcDingtalkDeptService {

    void addOcDingtalkDept(OcDingtalkDept ocDingtalkDept);

    void updateOcDingtalkDept(OcDingtalkDept ocDingtalkDept);

    void deleteOcDingtalkDept(int id);

    List<OcDingtalkDept> queryOcDingtalkDeptByParentId(Long parentId, String uid);

    List<OcDingtalkDept> queryOcDingtalkDeptAll();

    OcDingtalkDept queryOcDingtalkDeptByDeptId(Long deptId, String uid);
    List<OcDingtalkDept> queryDingtalkRootDept();
}
