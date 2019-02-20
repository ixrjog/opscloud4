package com.sdg.cmdb.service;

import com.aliyun.oss.model.OSSObjectSummary;

import java.util.List;

public interface AliyunOssService {

    List<OSSObjectSummary> listObject(String prefix);
}
