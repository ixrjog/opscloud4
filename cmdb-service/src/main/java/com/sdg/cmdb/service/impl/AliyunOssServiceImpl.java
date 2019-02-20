package com.sdg.cmdb.service.impl;

import com.aliyun.oss.OSS;

import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.internal.ResponseParsers;
import com.aliyun.oss.model.ListObjectsRequest;

import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;

import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.AliyunEcsItemEnum;
import com.sdg.cmdb.service.AliyunOssService;
import com.sdg.cmdb.service.ConfigCenterService;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class AliyunOssServiceImpl implements AliyunOssService {

    public static final String OC_BUCKET = "opscloud";

    private HashMap<String, String> configMap;

    @Resource
    private ConfigCenterService configCenterService;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.ALIYUN_ECS.getItemKey());
    }

    @Override
    public List<OSSObjectSummary> listObject(String prefix) {
        //IAcsClient iAcsClient = acqClient();
        ListObjectsRequest request = new ListObjectsRequest();

        request.setPrefix(prefix);

        HashMap<String, String> configMap = acqConifMap();
        String aliyunAccessKey = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_ACCESS_KEY.getItemKey());
        String aliyunAccessSecret = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_ACCESS_SECRET.getItemKey());

        // OSS client = acqClient();
        OSS client = new OSSClientBuilder().build("http://oss-cn-hangzhou.aliyuncs.com", aliyunAccessKey, aliyunAccessSecret);
        try {
            ObjectListing reponse = client.listObjects(OC_BUCKET, prefix);
            if (reponse != null) return reponse.getObjectSummaries();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
