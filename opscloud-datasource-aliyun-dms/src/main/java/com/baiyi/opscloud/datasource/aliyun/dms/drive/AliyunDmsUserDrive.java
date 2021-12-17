package com.baiyi.opscloud.datasource.aliyun.dms.drive;

import com.aliyun.dms_enterprise20181101.models.*;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.dms.client.DmsClinet;
import com.baiyi.opscloud.datasource.aliyun.dms.client.AliyunDmsClient;
import com.baiyi.opscloud.datasource.aliyun.dms.entity.DmsUser;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

import static com.baiyi.opscloud.datasource.aliyun.core.SimpleAliyunClient.Query.PAGE_SIZE;

/**
 * @Author baiyi
 * @Date 2021/12/16 2:29 PM
 * @Version 1.0
 */
public class AliyunDmsUserDrive {

    public static List<DmsUser.User> listUser(AliyunConfig.Aliyun aliyun) throws Exception {
        long tid = Optional.of(aliyun)
                .map(AliyunConfig.Aliyun::getDms)
                .map(AliyunConfig.Dms::getTid)
                .orElse(-1L);
        if (tid == -1L)
            throw new CommonRuntimeException("租户TID未配置！");
        return listUser(aliyun, tid);
    }

    public static List<DmsUser.User> listUser(AliyunConfig.Aliyun aliyun, Long tid) throws Exception {
        com.aliyun.dms_enterprise20181101.Client client = AliyunDmsClient.createClient(aliyun);
        ListUsersRequest request = new ListUsersRequest()
                .setPageSize(PAGE_SIZE)
                .setTid(tid);
        long size = PAGE_SIZE;
        int pageNumber = 1;
        List<DmsUser.User> users = Lists.newArrayList();
        while (PAGE_SIZE <= size) {
            request.setPageNumber(pageNumber);
            ListUsersResponse response = client.listUsers(request);
            List<ListUsersResponseBody.ListUsersResponseBodyUserListUser> list = response.getBody().getUserList().getUser();
            users.addAll(BeanCopierUtil.copyListProperties(list, DmsUser.User.class));
            size = response.getBody().getTotalCount();
            pageNumber++;
        }
        return users;
    }

    public static void registerUser(AliyunConfig.Aliyun aliyun, Long tid, DmsUser.User user) throws Exception {
        DmsClinet client = AliyunDmsClient.createMyClient(aliyun);
        // DINGDING
        RegisterUserRequest request = new RegisterUserRequest();
        request.setTid(tid);
        request.setUserNick(user.getNickName());
        request.setMobile(user.getMobile());
        request.setUid(user.getUid());
        request.setRoleNames("USER");
        RegisterUserResponse response = client.registerUser(request);
        if (!response.getBody().getSuccess())
            throw new CommonRuntimeException(response.getBody().errorMessage);
    }

}
