package com.baiyi.opscloud.dingtalk.handler;

import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserCountRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserListRequest;
import com.dingtalk.api.response.OapiUserCountResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 10:03 上午
 * @Since 1.0
 */

@Slf4j
@Component
public class DingtalkUserApiHandler extends DingtalkApiHandler {

    private interface UserApiUrls {
        String getUserList = "topapi/v2/user/list";
        String getUserDetail = "topapi/v2/user/get";
        String getUserCount = "topapi/user/count";
    }

    public OapiV2UserListResponse.PageResult getUserList(OapiV2UserListRequest request, String accessToken) {
        DingTalkClient client = getDingTalkClient(UserApiUrls.getUserList);
        try {
            OapiV2UserListResponse response = client.execute(request, accessToken);
            return checkResponse(response) ? response.getResult() : null;
        } catch (ApiException e) {
            log.error("查询部门用户完整信息失败", e);
            return null;
        }
    }

    public OapiV2UserGetResponse.UserGetRequest getUserDetail(String userId, String accessToken) {
        DingTalkClient client = getDingTalkClient(UserApiUrls.getUserDetail);
        OapiV2UserGetRequest request = new OapiV2UserGetRequest();
        request.setUserid(userId);
        try {
            OapiV2UserGetResponse response = client.execute(request, accessToken);
            return checkResponse(response) ? response.getResult() : null;
        } catch (ApiException e) {
            log.error("查询用户信息失败", e);
            return null;
        }
    }

    public Integer getUserCount(String accessToken) {
        DingTalkClient client = getDingTalkClient(UserApiUrls.getUserCount);
        OapiUserCountRequest request = new OapiUserCountRequest();
        request.setOnlyActive(true);
        try {
            OapiUserCountResponse response = client.execute(request, accessToken);
            return checkResponse(response) ? response.getResult().getCount().intValue() : 0;
        } catch (ApiException e) {
            log.error("获取活跃员工人数失败", e);
            return 0;
        }
    }

}
