package com.baiyi.opscloud.aliyun.core.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.vo.cloud.AliyunAccountVO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 6:52 下午
 * @Version 1.0
 */
@Component("AliyunCore")
public class AliyunCoreImpl implements AliyunCore {

    @Resource
    private AliyunCoreConfig aliyunCoreConfig;

    @Override
    public List<AliyunAccountVO.AliyunAccount> queryAliyunAccount() {
        return BeanCopierUtils.copyListProperties(getAccounts(), AliyunAccountVO.AliyunAccount.class);
    }

    @Override
    public List<AliyunCoreConfig.AliyunAccount> getAccounts() {
        return aliyunCoreConfig.getAccounts();
    }

    @Override
    public AliyunCoreConfig.AliyunAccount getAccount() {
        for (AliyunCoreConfig.AliyunAccount aliyunAccount : aliyunCoreConfig.getAccounts())
            if (aliyunAccount.getMaster())
                return aliyunAccount;
        return null;
    }

    @Override
    public List<String> getRegionIds() {
        for (AliyunCoreConfig.AliyunAccount aliyunAccount : aliyunCoreConfig.getAccounts())
            if (aliyunAccount.getMaster())
                return aliyunAccount.getRegionIds();
        return Collections.emptyList();
    }

    @Override
    public IAcsClient getAcsClient(String regionId) {
        for (AliyunCoreConfig.AliyunAccount aliyunAccount : aliyunCoreConfig.getAccounts())
            if (aliyunAccount.getMaster())
                return getAcsClient(regionId, aliyunAccount);
        return null;
    }

    @Override
    public IAcsClient getAcsClient(String regionId, AliyunCoreConfig.AliyunAccount aliyunAccount) {
        String defRegionId;
        if (StringUtils.isEmpty(aliyunAccount.getRegionId())) {
            defRegionId = aliyunAccount.getRegionId();
        } else {
            defRegionId = regionId;
        }
        IClientProfile profile = DefaultProfile.getProfile(defRegionId, aliyunAccount.getAccessKeyId(), aliyunAccount.getSecret());
        return new DefaultAcsClient(profile);
    }

    @Override
    public AliyunCoreConfig.AliyunAccount getAliyunAccountByUid(String uid) {
        for (AliyunCoreConfig.AliyunAccount aliyunAccount : aliyunCoreConfig.getAccounts())
            if (aliyunAccount.getUid().equals(uid))
                return aliyunAccount;
        return null;
    }

    @Override
    public IAcsClient getMasterClient() {
        AliyunCoreConfig.AliyunAccount master = getAccount();
        return getAcsClient(master.getRegionId());
    }
}
