package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbAccount;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbAttribute;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbDatabase;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudDBAccountVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudDBDatabaseVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudDBVO;
import com.baiyi.opscloud.service.cloud.OcCloudDBAccountService;
import com.baiyi.opscloud.service.cloud.OcCloudDBAttributeService;
import com.baiyi.opscloud.service.cloud.OcCloudDBDatabaseService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/3/1 1:48 下午
 * @Version 1.0
 */
@Component
public class CloudDBDecorator {

    @Resource
    private OcCloudDBAttributeService ocCloudDBAttributeService;

    @Resource
    private OcCloudDBDatabaseService ocCloudDBDatabaseService;

    @Resource
    private OcCloudDBAccountService ocCloudDBAccountService;

    private Map<String, String> getAttributeMap(List<OcCloudDbAttribute> attributeList) {
        return attributeList.stream().collect(Collectors.toMap(OcCloudDbAttribute::getAttributeName, OcCloudDbAttribute::getAttributeValue, (k1, k2) -> k1));
    }

    public OcCloudDBVO.CloudDB decorator(OcCloudDBVO.CloudDB cloudDB, Integer extend) {
        if (extend != null && extend == 1) {
            // 装饰属性
            List<OcCloudDbAttribute> attributeList = ocCloudDBAttributeService.queryOcCloudDbAttributeByCloudDbId(cloudDB.getId());
            cloudDB.setAttributeMap(getAttributeMap(attributeList));
            // 装饰数据库
            List<OcCloudDbDatabase> ocCloudDbDatabaseList = ocCloudDBDatabaseService.queryOcCloudDbDatabaseByCloudDbId(cloudDB.getId());
            cloudDB.setDatabases(BeanCopierUtils.copyListProperties(ocCloudDbDatabaseList, OcCloudDBDatabaseVO.CloudDBDatabase.class));
            // 装饰账户
            List<OcCloudDbAccount> ocCloudDbAccountList = ocCloudDBAccountService.queryOcCloudDbAccountByCloudDbId(cloudDB.getId());
            cloudDB.setAccounts(BeanCopierUtils.copyListProperties(ocCloudDbAccountList, OcCloudDBAccountVO.CloudDBAccount.class));
            cloudDB.setPrivileges(ocCloudDbAccountList.stream().map(e -> e.getAccountPrivilege()).collect(Collectors.toList()));
        }
        return cloudDB;
    }
}
