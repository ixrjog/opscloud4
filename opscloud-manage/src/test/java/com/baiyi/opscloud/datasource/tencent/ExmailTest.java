package com.baiyi.opscloud.datasource.tencent;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.TencentExmailConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.tencent.exmail.entry.ExmailToken;
import com.baiyi.opscloud.tencent.exmail.entry.ExmailUser;
import com.baiyi.opscloud.tencent.exmail.datasource.TencentExmailTokenDatasource;
import com.baiyi.opscloud.tencent.exmail.datasource.TencentExmailUserDatasource;
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
    private DsConfigHelper dsFactory;

    @Resource
    private TencentExmailUserDatasource tencentExmailUserHandler;

    @Resource
    private TencentExmailTokenDatasource tencentExmailTokenHandler;

    private TencentExmailConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(11);
        return dsFactory.build(datasourceConfig, TencentExmailConfig.class);
    }

    @Test
    void getUser() {
        TencentExmailConfig.Tencent config = getConfig().getTencent();
        ExmailUser bo = tencentExmailUserHandler.get(config, "baiyi@xinc818.group");
        System.err.println(bo);
    }

    @Test
    void listUser() {
        TencentExmailConfig.Tencent config = getConfig().getTencent();
        List<ExmailUser> list = tencentExmailUserHandler.list(config, 1L);
        System.err.println(list);
    }

    @Test
    void getTokenTest() {
        TencentExmailConfig.Tencent config = getConfig().getTencent();
        ExmailToken token = tencentExmailTokenHandler.getToken(config);
        System.out.println(token);
    }
}
