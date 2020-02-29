package com.baiyi.opscloud.cloud.db.impl;

import com.baiyi.opscloud.cloud.account.CloudAccount;
import com.baiyi.opscloud.cloud.db.ICloudDB;
import com.baiyi.opscloud.cloud.db.factory.CloudDBFactory;
import com.baiyi.opscloud.domain.generator.OcCloudDb;
import com.baiyi.opscloud.domain.generator.OcCloudDbAttribute;
import com.baiyi.opscloud.service.cloudDB.OcCloudDBAttributeService;
import com.baiyi.opscloud.service.cloudDB.OcCloudDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/2/28 7:05 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseCloudDB<T> implements InitializingBean, ICloudDB {

    @Resource
    private OcCloudDBService ocCloudDBService;

    @Resource
    private OcCloudDBAttributeService ocCloudDBAttributeService;

    @Override
    public Boolean syncDBInstance() {
        try {
            Map<String, List<T>> dbInstanceMap = getDBInstanceMap();
            for (String uid : dbInstanceMap.keySet()) {
                CloudAccount cloudAccount = getCloudAccountByUid(uid);
                if (cloudAccount == null) continue;
                List<T> dbInstanceList = dbInstanceMap.get(uid);
                for (T dbInstance : dbInstanceList)
                    saveOcCloudDb(cloudAccount, dbInstance);
            }
            // 保存属性
            saveDBInstanceAttribute(dbInstanceMap);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    protected void saveOcCloudDbAttributeList(String dbInstanceId, List<OcCloudDbAttribute> attributeList) {
        OcCloudDb ocCloudDb = ocCloudDBService.queryOcCloudDbByUniqueKey(getCloudDBType(), dbInstanceId);
        if (ocCloudDb == null) return;
        for (OcCloudDbAttribute preOcCloudDbAttribute : attributeList) {
            preOcCloudDbAttribute.setCloudDbId(ocCloudDb.getId());
            OcCloudDbAttribute ocCloudDbAttribute = ocCloudDBAttributeService.queryOcCloudDbAttributeByUniqueKey(ocCloudDb.getId(), preOcCloudDbAttribute.getAttributeName());
            if (ocCloudDbAttribute == null) {
                ocCloudDBAttributeService.addOcCloudDbAttribute(preOcCloudDbAttribute);
            } else {
                preOcCloudDbAttribute.setId(ocCloudDbAttribute.getId());
                ocCloudDBAttributeService.updateOcCloudDbAttribute(preOcCloudDbAttribute);
            }
        }
    }

    abstract protected int getCloudDBType();

    abstract protected CloudAccount getCloudAccountByUid(String uid);

    abstract protected Map<String, List<T>> getDBInstanceMap() throws Exception;

    abstract protected void saveDBInstanceAttribute(Map<String, List<T>> dbInstanceMap);

    abstract protected String getDBInstanceId(T dbInstance) throws Exception;

    abstract protected OcCloudDb getOcCloudDb(CloudAccount cloudAccount, T dbInstance) throws Exception;

    protected void saveOcCloudDb(CloudAccount cloudAccount, T dbInstance) {
        try {
            String dbInstanceId = getDBInstanceId(dbInstance);
            OcCloudDb ocCloudDb = ocCloudDBService.queryOcCloudDbByUniqueKey(getCloudDBType(), dbInstanceId);
            if (ocCloudDb == null) {
                ocCloudDb = getOcCloudDb(cloudAccount, dbInstance);
                ocCloudDBService.addOcCloudDb(ocCloudDb);
            } else {
                OcCloudDb preOcCloudDb = getOcCloudDb(cloudAccount, dbInstance);
                preOcCloudDb.setId(ocCloudDb.getId());
                ocCloudDBService.updateOcCloudDb(preOcCloudDb);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public String getKey() {
        return this.getClass().getSimpleName();
    }

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        CloudDBFactory.register(this);
    }

}
