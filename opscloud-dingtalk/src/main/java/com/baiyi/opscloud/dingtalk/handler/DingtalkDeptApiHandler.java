package com.baiyi.opscloud.dingtalk.handler;

import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2DepartmentListsubRequest;
import com.dingtalk.api.request.OapiV2DepartmentListsubidRequest;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubidResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 10:29 上午
 * @Since 1.0
 */
@Slf4j
@Component
public class DingtalkDeptApiHandler extends DingtalkApiHandler {

    private interface DeptApiUrls {
        String getDeptList = "topapi/v2/department/listsub";
        String getDeptIdList = "topapi/v2/department/listsubid";
    }

    public OapiV2DepartmentListsubResponse getDeptList(DingtalkParam.QueryByDeptId param) {
        DingTalkClient client = getDingTalkClient(DeptApiUrls.getDeptList);
        OapiV2DepartmentListsubRequest request = new OapiV2DepartmentListsubRequest();
        request.setDeptId(param.getDeptId());
        try {
            OapiV2DepartmentListsubResponse response = client.execute(request, param.getAccessToken());
            return checkResponse(response) ? response : null;
        } catch (ApiException e) {
            log.error("获取部门列表数据失败", e);
            return null;
        }
    }

    public List<Long> getDeptIdList(DingtalkParam.QueryByDeptId param) {
        DingTalkClient client = getDingTalkClient(DeptApiUrls.getDeptIdList);
        OapiV2DepartmentListsubidRequest request = new OapiV2DepartmentListsubidRequest();
        request.setDeptId(param.getDeptId());
        try {
            OapiV2DepartmentListsubidResponse response = client.execute(request, param.getAccessToken());
            return checkResponse(response) ? response.getResult().getDeptIdList() : Collections.emptyList();
        } catch (ApiException e) {
            log.error("获取部门id列表数据失败", e);
            return Collections.emptyList();
        }
    }
}
