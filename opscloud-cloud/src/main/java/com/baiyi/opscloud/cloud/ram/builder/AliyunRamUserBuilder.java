package com.baiyi.opscloud.cloud.ram.builder;

import com.aliyuncs.ram.model.v20150501.CreateUserResponse;
import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.cloud.ram.bo.AliyunRamUserBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;

/**
 * @Author baiyi
 * @Date 2020/6/9 1:37 下午
 * @Version 1.0
 */
public class AliyunRamUserBuilder {

    public static OcAliyunRamUser build(AliyunCoreConfig.AliyunAccount aliyunAccount, ListUsersResponse.User user) {
        AliyunRamUserBO aliyunRamUserBO = AliyunRamUserBO.builder()
                .accountUid(aliyunAccount.getUid())
                .ramUserId(user.getUserId())
                .ramUsername(user.getUserName())
                .ramDisplayName(user.getDisplayName())
                .mobile(user.getMobilePhone())
                .createDate(TimeUtils.acqGmtDate(user.getCreateDate()))
                .updateDate(TimeUtils.acqGmtDate(user.getUpdateDate()))
                .comment(user.getComments())
                .build();
        return convert(aliyunRamUserBO);
    }

    public static OcAliyunRamUser build(AliyunCoreConfig.AliyunAccount aliyunAccount, GetUserResponse.User user) {
        AliyunRamUserBO aliyunRamUserBO = AliyunRamUserBO.builder()
                .accountUid(aliyunAccount.getUid())
                .ramUserId(user.getUserId())
                .ramUsername(user.getUserName())
                .ramDisplayName(user.getDisplayName())
                .mobile(user.getMobilePhone())
                .createDate(TimeUtils.acqGmtDate(user.getCreateDate()))
                .updateDate(TimeUtils.acqGmtDate(user.getUpdateDate()))
                .comment(user.getComments())
                .build();
        return convert(aliyunRamUserBO);
    }

    public static OcAliyunRamUser build(AliyunCoreConfig.AliyunAccount aliyunAccount, CreateUserResponse.User user) {
        AliyunRamUserBO aliyunRamUserBO = AliyunRamUserBO.builder()
                .accountUid(aliyunAccount.getUid())
                .ramUserId(user.getUserId())
                .ramUsername(user.getUserName())
                .ramDisplayName(user.getDisplayName())
                .mobile(user.getMobilePhone())
                .createDate(TimeUtils.acqGmtDate(user.getCreateDate()))
                .comment(user.getComments())
                .build();
        return convert(aliyunRamUserBO);
    }

    private static OcAliyunRamUser convert(AliyunRamUserBO aliyunRamUserBO) {
        return BeanCopierUtils.copyProperties(aliyunRamUserBO, OcAliyunRamUser.class);
    }

}
