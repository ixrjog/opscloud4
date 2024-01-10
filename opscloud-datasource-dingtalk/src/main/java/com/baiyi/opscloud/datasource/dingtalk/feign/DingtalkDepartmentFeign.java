package com.baiyi.opscloud.datasource.dingtalk.feign;

import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkDepartment;
import com.baiyi.opscloud.datasource.dingtalk.param.DingtalkDepartmentParam;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2021/11/29 5:51 下午
 * @Version 1.0
 */
public interface DingtalkDepartmentFeign {

    @RequestLine("POST /topapi/v2/department/listsubid?access_token={accessToken}")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    DingtalkDepartment.DepartmentSubIdResponse listSubId(@Param("accessToken") String accessToken, DingtalkDepartmentParam.ListSubDepartmentId listSubDepartmentId);

    @RequestLine("POST /topapi/v2/department/get?access_token={accessToken}")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    DingtalkDepartment.GetDepartmentResponse get(@Param("accessToken") String accessToken, DingtalkDepartmentParam.GetDepartment getDepartment);

}