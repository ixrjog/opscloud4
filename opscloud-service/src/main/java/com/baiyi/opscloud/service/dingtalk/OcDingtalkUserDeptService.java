package com.baiyi.opscloud.service.dingtalk;

import com.baiyi.opscloud.domain.generator.opscloud.OcDingtalkUserDept;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/9 2:56 下午
 * @Since 1.0
 */
public interface OcDingtalkUserDeptService {

    void addOcDingtalkUserDeptList(List<OcDingtalkUserDept> ocDingtalkUserDeptList);

    void delOcDingtalkUserDeptByDeptId(Integer ocDingtalkDeptId);

    List<OcDingtalkUserDept> queryOcDingtalkUserDeptByDeptId(Integer ocDingtalkDeptId);
}
