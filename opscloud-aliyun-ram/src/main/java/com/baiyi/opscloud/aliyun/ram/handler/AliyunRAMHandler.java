package com.baiyi.opscloud.aliyun.ram.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.ram.model.v20150501.ListUsersRequest;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/9 11:11 上午
 * @Version 1.0
 */
@Component
public class AliyunRAMHandler {

    @Resource
    private AliyunCore aliyunCore;

    public static final int MAX_ITEMS = 100;

    /**
     * 查询账户下所有用户
     * @param aliyunAccount
     * @return
     */
    public List<ListUsersResponse.User> getUsers(AliyunAccount aliyunAccount) {
        IAcsClient client = acqAcsClient(aliyunAccount.getRegionId(), aliyunAccount);
        List<ListUsersResponse.User> users = Lists.newArrayList();
        String marker = "";
        while (true) {
            ListUsersResponse responseMarker = listUsers(client, marker);
            users.addAll(responseMarker.getUsers());
            if (!responseMarker.getIsTruncated()) {
                return users;
            } else {
                marker = responseMarker.getMarker();
            }
        }
    }

    private ListUsersResponse listUsers(IAcsClient client, String marker) {
        ListUsersRequest request = new ListUsersRequest();
        request.setMaxItems(MAX_ITEMS);
        if (!StringUtils.isEmpty(marker))
            request.setMarker(marker);
        try {
            ListUsersResponse response
                    = client.getAcsResponse(request);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }


    private IAcsClient acqAcsClient(String regionId, AliyunAccount aliyunAccount) {
        return aliyunCore.getAcsClient(regionId, aliyunAccount);
    }
}
