package com.baiyi.opscloud.dingtalk.impl;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.dingtalk.DingtalkTokenApi;
import com.baiyi.opscloud.dingtalk.DingtalkUserApi;
import com.baiyi.opscloud.dingtalk.bo.DingtalkUserBO;
import com.baiyi.opscloud.dingtalk.convert.DingtalkUserConvert;
import com.baiyi.opscloud.dingtalk.handler.DingtalkUserApiHandler;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import com.dingtalk.api.request.OapiV2UserListRequest;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 4:23 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class DingtalkUserApoImpl implements DingtalkUserApi {

    @Resource
    private DingtalkUserApiHandler dingtalkUserApiHandler;

    @Resource
    private DingtalkTokenApi dingtalkTokenApi;

    private List<OapiV2UserListResponse.ListUserResponse> getDeptUserList(String uid, Long deptId) {
        String accessToken = dingtalkTokenApi.getDingtalkToken(uid);
        final Long size = 100L;
        Long cursor = 0L;
        OapiV2UserListRequest request = new OapiV2UserListRequest();
        request.setDeptId(deptId);
        request.setCursor(cursor);
        request.setSize(size);
        request.setOrderField("entry_asc");
        List<OapiV2UserListResponse.ListUserResponse> userList = Lists.newArrayList();
        boolean hasMore = false;
        do {
            try {
                OapiV2UserListResponse.PageResult userPageData = getDeptUserList(request, accessToken);
                userList.addAll(userPageData.getList());
                hasMore = userPageData.getHasMore();
                cursor = userPageData.getNextCursor();
                request.setCursor(cursor);
            } catch (RuntimeException ignored) {
            }
        } while (hasMore);
        return userList;
    }

    @Retryable(value = RuntimeException.class, backoff = @Backoff(delay = 1000))
    private OapiV2UserListResponse.PageResult getDeptUserList(OapiV2UserListRequest request, String accessToken) {
        OapiV2UserListResponse.PageResult userPageData = dingtalkUserApiHandler.getUserList(request, accessToken);
        if (userPageData == null)
            throw new RuntimeException("获取dingtalk dept 失败,retry");
        return userPageData;
    }

    @Override
    public List<DingtalkUserBO> getDeptUserList(DingtalkParam.QueryByDeptId param) {
        List<OapiV2UserListResponse.ListUserResponse> userResponseList = getDeptUserList(param.getUid(), param.getDeptId());
        return DingtalkUserConvert.toBOList(userResponseList, param.getUid());
    }

    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_COMMON_BY_DAY, key = "'dingtalkUserDetail_' + #userId", unless = "#result == null")
    public DingtalkUserBO getUserDetail(String userId, String uid) {
        String accessToken = dingtalkTokenApi.getDingtalkToken(uid);
        OapiV2UserGetResponse.UserGetRequest userDetail = dingtalkUserApiHandler.getUserDetail(userId, accessToken);
        if (userDetail != null)
            return DingtalkUserConvert.toBO(userDetail, uid);
        return null;
    }

    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_COMMON_BY_DAY, key = "'dingtalkUserDetail_' + #userId")
    public void evictPreview(String userId) {
    }

    @Override
    public DingtalkUserBO refreshUserDetail(String userId, String uid) {
        evictPreview(userId);
        return getUserDetail(userId, uid);
    }

    @Override
    public Integer getUserCount(String uid) {
        String accessToken = dingtalkTokenApi.getDingtalkToken(uid);
        return dingtalkUserApiHandler.getUserCount(accessToken);
    }

}
