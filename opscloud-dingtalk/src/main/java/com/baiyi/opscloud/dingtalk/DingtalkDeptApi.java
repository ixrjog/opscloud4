package com.baiyi.opscloud.dingtalk;

import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 3:17 下午
 * @Since 1.0
 */
public interface DingtalkDeptApi {

    List<OapiV2DepartmentListsubResponse.DeptBaseResponse> getDeptList(DingtalkParam.QueryByDeptId param) throws RuntimeException;

    List<Long> getDeptIdList(DingtalkParam.QueryByDeptId param);
}
