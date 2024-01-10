package com.baiyi.opscloud.datasource.aliyun.cms.driver;

import com.aliyuncs.cms.model.v20190101.DescribeContactListRequest;
import com.aliyuncs.cms.model.v20190101.DescribeContactListResponse;
import com.aliyuncs.cms.model.v20190101.PutContactRequest;
import com.aliyuncs.cms.model.v20190101.PutContactResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.DingtalkUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/7/13 13:31
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunCmsContactDriver {

    private final AliyunClient aliyunClient;

    public interface Query {
        int PAGE_SIZE = 50;
    }

    /**
     * @param regionId
     * @param aliyun
     * @return
     */
    public List<DescribeContactListResponse.Contact> listContacts(String regionId, AliyunConfig.Aliyun aliyun) {
        List<DescribeContactListResponse.Contact> contacts = Lists.newArrayList();
        DescribeContactListRequest request = new DescribeContactListRequest();
        request.setSysRegionId(regionId);
        request.setPageSize(Query.PAGE_SIZE);
        request.setPageSize(Query.PAGE_SIZE);
        int size = Query.PAGE_SIZE;
        int pageNumber = 1;
        try {
            while (Query.PAGE_SIZE <= size) {
                request.setPageNumber(pageNumber);
                DescribeContactListResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
                contacts.addAll(response.getContacts());
                size = response.getContacts().size();
                pageNumber++;
            }
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        return contacts;
    }

    /**
     * 创建告警用户
     *
     * @param regionId
     * @param aliyun
     * @param user
     * @return
     */
    public boolean putContact(String regionId, AliyunConfig.Aliyun aliyun, User user) {
        PutContactRequest request = new PutContactRequest();
        request.setSysRegionId(regionId);
        request.setContactName(user.getDisplayName());
        request.setDescribe(user.getUsername());
        if (StringUtils.isNotBlank(user.getPhone())) {
            request.setChannelsSMS(user.getPhone());
        }
        if (StringUtils.isNotBlank(user.getEmail())) {
            request.setChannelsMail(user.getEmail());
        }
        Optional<String> optionalToken = Optional.ofNullable(aliyun)
                .map(AliyunConfig.Aliyun::getCms)
                .map(AliyunConfig.Cms::getDingtalk)
                .map(AliyunConfig.Dingtalk::getToken);
        optionalToken.ifPresent(s -> request.setChannelsDingWebHook(DingtalkUtil.getRobotWebHook(s)));
        try {
            PutContactResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            return response.getSuccess();
        } catch (ClientException e) {
            log.error(e.getMessage());
            return false;
        }
    }

}