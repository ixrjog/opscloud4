package com.baiyi.opscloud.aliyun.ons.impl;

import com.aliyuncs.ons.model.v20190214.OnsConsumerStatusResponse;
import com.aliyuncs.ons.model.v20190214.OnsGroupListResponse;
import com.aliyuncs.ons.model.v20190214.OnsGroupSubDetailResponse;
import com.baiyi.opscloud.aliyun.ons.AliyunONSGroup;
import com.baiyi.opscloud.aliyun.ons.handler.AliyunONSGroupHandler;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 3:43 下午
 * @Since 1.0
 */

@Component("AliyunONSGroup")
public class AliyunONSGroupImpl implements AliyunONSGroup {

    @Resource
    private AliyunONSGroupHandler aliyunONSGroupHandler;

    @Override
    public List<OnsGroupListResponse.SubscribeInfoDo> queryOnsGroupList(AliyunONSParam.QueryGroupList param) {
        return aliyunONSGroupHandler.queryOnsGroupList(param);
    }

    @Override
    public OnsGroupSubDetailResponse.Data queryOnsGroupSubDetail(AliyunONSParam.QueryGroupSubDetail param) {
        return aliyunONSGroupHandler.queryOnsGroupSubDetail(param);
    }

    @Override
    @Retryable(value = RuntimeException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 1.5))
    public Boolean onsGroupCreate(AliyunONSParam.GroupCreate param) {
        Boolean result = aliyunONSGroupHandler.onsGroupCreate(param);
        if (!result)
            throw new RuntimeException("创建GroupId失败,retry");
        return true;
    }

    @Override
    public OnsGroupListResponse.SubscribeInfoDo queryOnsGroup(AliyunONSParam.QueryGroup param) {
        return aliyunONSGroupHandler.queryOnsGroup(param);
    }

    @Override
    @Retryable(value = RuntimeException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 1.5))
    public OnsConsumerStatusResponse.Data onsGroupStatus(AliyunONSParam.QueryGroupSubDetail param, Boolean needDetail) throws RuntimeException {
        OnsConsumerStatusResponse.Data data = aliyunONSGroupHandler.onsGroupStatus(param, needDetail);
        if (data == null)
            throw new RuntimeException("查询GroupId详细信息失败,retry");
        else
            return data;
    }
}
