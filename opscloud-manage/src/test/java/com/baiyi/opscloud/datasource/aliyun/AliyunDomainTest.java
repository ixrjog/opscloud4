package com.baiyi.opscloud.datasource.aliyun;

import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.base.BaseAliyunTest;
import com.baiyi.opscloud.datasource.aliyun.domain.driver.AliyunDomainDriver;
import com.baiyi.opscloud.datasource.aliyun.domain.entity.AliyunDomain;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/4/18 13:34
 * @Version 1.0
 */
public class AliyunDomainTest extends BaseAliyunTest {

    @Resource
    private AliyunDomainDriver aliyunDomainDriver;

    @Test
    void listDomainsTest() {
        AliyunConfig config = getConfig();
        try {
            List<AliyunDomain.Domain> domains = aliyunDomainDriver.listDomains(config.getAliyun().getRegionId(), config.getAliyun());
            print(domains);
            for (AliyunDomain.Domain domain : domains) {
                AssetContainer ac= domain.toAssetContainer(null);
                print(ac);
            }
        } catch (Exception e) {
            print(e);
        }
    }

}
