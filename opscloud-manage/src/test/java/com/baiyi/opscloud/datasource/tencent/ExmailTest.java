package com.baiyi.opscloud.datasource.tencent;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.TencentExmailDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsTencentExmailConfig;
import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.tencent.exmail.entry.ExmailUser;
import com.baiyi.opscloud.tencent.exmail.handler.TencentExmailUserHandler;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/12 7:05 下午
 * @Since 1.0
 */
public class ExmailTest extends BaseUnit {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigFactory dsFactory;

    @Resource
    private TencentExmailUserHandler tencentExmailUserHandler;

    private TencentExmailDsInstanceConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(11);
        return dsFactory.build(datasourceConfig, TencentExmailDsInstanceConfig.class);
    }

    @Test
    void getUser() {
        DsTencentExmailConfig.Tencent config = getConfig().getTencent();
        ExmailUser bo = tencentExmailUserHandler.getUser(config, "baiyi@xinc818.group");
        System.err.println(bo);
    }

    @Test
    void listUser() {
        DsTencentExmailConfig.Tencent config = getConfig().getTencent();
        List<ExmailUser> list = tencentExmailUserHandler.listUser(config, 1L);
        System.err.println(list);
    }
}
