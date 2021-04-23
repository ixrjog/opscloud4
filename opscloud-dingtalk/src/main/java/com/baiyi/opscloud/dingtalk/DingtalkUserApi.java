package com.baiyi.opscloud.dingtalk;

import com.baiyi.opscloud.dingtalk.bo.DingtalkUserBO;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 4:23 下午
 * @Since 1.0
 */
public interface DingtalkUserApi {

    List<DingtalkUserBO> getDeptUserList(DingtalkParam.QueryByDeptId param);

    DingtalkUserBO getUserDetail(String userId, String uid);

    DingtalkUserBO refreshUserDetail(String userId, String uid);

    Integer getUserCount(String uid);
}
