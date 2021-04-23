package com.baiyi.opscloud.cloud.mq;

import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/5 1:54 下午
 * @Since 1.0
 */
public interface AliyunONSInstanceCenter {

    Boolean syncONSInstance(String regionId);

    Boolean updateONSInstanceDetail(AliyunONSParam.QueryInstanceDetail param);

}
