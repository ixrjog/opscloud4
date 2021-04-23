package com.baiyi.opscloud.aliyun.core;

import com.aliyuncs.IAcsClient;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.domain.vo.cloud.AliyunAccountVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 6:52 下午
 * @Version 1.0
 */
public interface AliyunCore {

    List<String> getRegionIds();

    /**
     * 对外查询主账户列表
     * @return
     */
    List<AliyunAccountVO.AliyunAccount> queryAliyunAccount();

    List<AliyunCoreConfig.AliyunAccount> getAccounts();

    /**
     * 返回主账户
     *
     * @return
     */
    AliyunCoreConfig.AliyunAccount getAccount();

    IAcsClient getAcsClient(String regionId);

    IAcsClient getAcsClient(String regionId, AliyunCoreConfig.AliyunAccount aliyunAccount);

    AliyunCoreConfig.AliyunAccount getAliyunAccountByUid(String uid);

    IAcsClient getMasterClient();
}
