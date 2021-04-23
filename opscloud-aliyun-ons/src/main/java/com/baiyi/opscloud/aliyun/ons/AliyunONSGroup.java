package com.baiyi.opscloud.aliyun.ons;

import com.aliyuncs.ons.model.v20190214.OnsConsumerStatusResponse;
import com.aliyuncs.ons.model.v20190214.OnsGroupListResponse;
import com.aliyuncs.ons.model.v20190214.OnsGroupSubDetailResponse;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 3:43 下午
 * @Since 1.0
 */
public interface AliyunONSGroup {

    List<OnsGroupListResponse.SubscribeInfoDo> queryOnsGroupList(AliyunONSParam.QueryGroupList param);

    OnsGroupSubDetailResponse.Data queryOnsGroupSubDetail(AliyunONSParam.QueryGroupSubDetail param);

    Boolean onsGroupCreate(AliyunONSParam.GroupCreate param);

    OnsGroupListResponse.SubscribeInfoDo queryOnsGroup(AliyunONSParam.QueryGroup param);

    OnsConsumerStatusResponse.Data onsGroupStatus(AliyunONSParam.QueryGroupSubDetail param, Boolean needDetail) throws RuntimeException;
}
