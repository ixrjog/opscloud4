package com.baiyi.opscloud.dingtalk.convert;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.dingtalk.bo.DingtalkUserBO;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 6:13 下午
 * @Since 1.0
 */
public class DingtalkUserConvert {

    public static DingtalkUserBO toBO(OapiV2UserListResponse.ListUserResponse userResponse, String uid) {
        DingtalkUserBO userBO = BeanCopierUtils.copyProperties(userResponse, DingtalkUserBO.class);
        userBO.setUid(uid);
        return userBO;
    }

    public static List<DingtalkUserBO> toBOList(List<OapiV2UserListResponse.ListUserResponse> userResponseList, String uid) {
        List<DingtalkUserBO> userBOList = Lists.newArrayListWithCapacity(userResponseList.size());
        userResponseList.forEach(userResponse -> userBOList.add(toBO(userResponse, uid)));
        return userBOList;
    }

    public static DingtalkUserBO toBO(OapiV2UserGetResponse.UserGetRequest user, String uid) {
        DingtalkUserBO userBO = BeanCopierUtils.copyProperties(user, DingtalkUserBO.class);
        userBO.setUid(uid);
        return userBO;
    }
}
