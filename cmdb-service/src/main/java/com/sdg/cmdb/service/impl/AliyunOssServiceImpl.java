package com.sdg.cmdb.service.impl;

import com.aliyun.oss.OSS;

import com.aliyun.oss.model.ListObjectsRequest;

import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;

import com.sdg.cmdb.service.AliyunOssService;
import com.sdg.cmdb.service.AliyunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


@Service
public class AliyunOssServiceImpl implements AliyunOssService {

    public static final String OC_BUCKET = "opscloud";

    public static final String ALIYUN_OSS_ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";


    @Autowired
    private AliyunService aliyunService;


    @Override
    public List<OSSObjectSummary> listObject(String prefix) {
        //IAcsClient iAcsClient = acqClient();
        ListObjectsRequest request = new ListObjectsRequest();

        request.setPrefix(prefix);

        // OSS client = acqClient();
        OSS client = aliyunService.acqOSSClient(ALIYUN_OSS_ENDPOINT);
        try {
            ObjectListing reponse = client.listObjects(OC_BUCKET, prefix);
            if (reponse != null) return reponse.getObjectSummaries();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
