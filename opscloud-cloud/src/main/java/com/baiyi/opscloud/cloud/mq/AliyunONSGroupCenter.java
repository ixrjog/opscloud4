package com.baiyi.opscloud.cloud.mq;

import com.aliyuncs.ons.model.v20190214.OnsConsumerStatusResponse;
import com.aliyuncs.ons.model.v20190214.OnsGroupSubDetailResponse;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 3:56 下午
 * @Since 1.0
 */
public interface AliyunONSGroupCenter {

    Boolean syncONSGroup(AliyunONSParam.QueryGroupList param);

    OnsGroupSubDetailResponse.Data queryOnsGroupSubDetail(AliyunONSParam.QueryGroupSubDetail param);

    Boolean onsGroupCreate(AliyunONSParam.GroupCreate param);

    Boolean saveGroup(AliyunONSParam.QueryGroup param);

    OnsConsumerStatusResponse.Data onsGroupStatus(AliyunONSParam.QueryGroupSubDetail param, Boolean needDetail);
}
