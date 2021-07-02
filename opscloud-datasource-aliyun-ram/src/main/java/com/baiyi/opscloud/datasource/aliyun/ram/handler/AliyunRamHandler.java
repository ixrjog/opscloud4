package com.baiyi.opscloud.datasource.aliyun.ram.handler;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.ListPoliciesRequest;
import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.aliyuncs.ram.model.v20150501.ListUsersRequest;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.opscloud.common.datasource.config.DsAliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.core.handler.AliyunHandler;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.datasource.aliyun.core.common.BaseAliyunHandler.Query.PAGE_SIZE;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 7:40 下午
 * @Since 1.0
 */

@Component
public class AliyunRamHandler {

    @Resource
    private AliyunHandler aliyunHandler;

    public List<ListUsersResponse.User> listUsers(String regionId, DsAliyunConfig.Aliyun aliyun) {
        List<ListUsersResponse.User> userList = Lists.newArrayList();
        String marker;
        try {
            ListUsersRequest request = new ListUsersRequest();
            request.setMaxItems(PAGE_SIZE);
            do {
                ListUsersResponse response = aliyunHandler.getAcsResponse(regionId, aliyun, request);
                userList.addAll(response.getUsers());
                marker = response.getMarker();
                request.setMarker(marker);
            } while (Strings.isNotBlank(marker));
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public List<ListPoliciesResponse.Policy> listPolicies(String regionId, DsAliyunConfig.Aliyun aliyun) {
        List<ListPoliciesResponse.Policy> policyList = Lists.newArrayList();
        String marker;
        try {
            ListPoliciesRequest request = new ListPoliciesRequest();
            request.setMaxItems(PAGE_SIZE);
            do {
                ListPoliciesResponse response = aliyunHandler.getAcsResponse(regionId, aliyun, request);
                policyList.addAll(response.getPolicies());
                marker = response.getMarker();
                request.setMarker(marker);
            } while (Strings.isNotBlank(marker));
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return policyList;
    }
}
