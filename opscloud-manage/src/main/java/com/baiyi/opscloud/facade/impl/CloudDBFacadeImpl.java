package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.cloud.db.ICloudDB;
import com.baiyi.opscloud.cloud.db.config.CloudDBConfig;
import com.baiyi.opscloud.cloud.db.factory.CloudDBFactory;
import com.baiyi.opscloud.common.base.CloudDBType;
import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.PasswordUtils;
import com.baiyi.opscloud.decorator.cloud.CloudDBDatabaseDecorator;
import com.baiyi.opscloud.decorator.cloud.CloudDBDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDb;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbAccount;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbAttribute;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbDatabase;
import com.baiyi.opscloud.domain.param.cloud.CloudDBDatabaseParam;
import com.baiyi.opscloud.domain.param.cloud.CloudDBParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudDatabaseSlowLogVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudDBAccountVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudDBDatabaseVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudDBVO;
import com.baiyi.opscloud.facade.CloudDBFacade;
import com.baiyi.opscloud.service.cloud.OcCloudDBAccountService;
import com.baiyi.opscloud.service.cloud.OcCloudDBAttributeService;
import com.baiyi.opscloud.service.cloud.OcCloudDBDatabaseService;
import com.baiyi.opscloud.service.cloud.OcCloudDBService;
import com.google.common.base.Joiner;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/3/1 12:20 下午
 * @Version 1.0
 */
@Service
public class CloudDBFacadeImpl implements CloudDBFacade {

    // 权限配置项
    public static final String[] PRIVILEGE_OPTIONS = {"ReadOnly", "ReadWrite", "DDLOnly", "DMLOnly"};

    @Resource
    private OcCloudDBService ocCloudDBService;

    @Resource
    private OcCloudDBDatabaseService ocCloudDBDatabaseService;

    @Resource
    private OcCloudDBAttributeService ocCloudDBAttributeService;


    @Resource
    private OcCloudDBAccountService ocCloudDBAccountService;

    @Resource
    private CloudDBDecorator cloudDBDecorator;

    @Resource
    private CloudDBDatabaseDecorator cloudDBDatabaseDecorator;

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    protected CloudDBConfig cloudDBConfig;

    @Override
    public DataTable<CloudDBVO.CloudDB> fuzzyQueryCloudDBPage(CloudDBParam.PageQuery pageQuery) {
        DataTable<OcCloudDb> table = ocCloudDBService.fuzzyQueryOcCloudDBByParam(pageQuery);
        List<CloudDBVO.CloudDB> page = BeanCopierUtils.copyListProperties(table.getData(), CloudDBVO.CloudDB.class);
        return new DataTable<>(page.stream().map(e -> cloudDBDecorator.decorator(e, 1)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    @Transactional
    public BusinessWrapper<Boolean> deleteCloudDBById(int id) {
        OcCloudDb ocCloudDb = ocCloudDBService.queryOcCloudDbById(id);
        if (ocCloudDb == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_DB_NOT_EXIST);
        ocCloudDBService.deleteOcCloudDbById(id);
        // 删除实例属性
        List<OcCloudDbAttribute> ocCloudDbAttributeList = ocCloudDBAttributeService.queryOcCloudDbAttributeByCloudDbId(id);
        for (OcCloudDbAttribute ocCloudDbAttribute : ocCloudDbAttributeList)
            ocCloudDBAttributeService.delOcCloudDbAttributeById(ocCloudDbAttribute.getId());
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> syncCloudDB() {
        Map<String, ICloudDB> context = CloudDBFactory.getCloudDBContainer();
        context.keySet().forEach(k -> {
            try {
                context.get(k).syncDBInstance();
            } catch (Exception e) {
            }
        });
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> syncCloudDatabase(int id) {
        OcCloudDb ocCloudDb = ocCloudDBService.queryOcCloudDbById(id);
        if (ocCloudDb == null) return new BusinessWrapper(ErrorEnum.CLOUD_DB_NOT_EXIST);
        String key = CloudDBType.getName(ocCloudDb.getCloudDbType());
        CloudDBFactory.getCloudDBByKey(key).syncDatabase(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<CloudDBDatabaseVO.CloudDBDatabase> fuzzyQueryCloudDBDatabasePage(CloudDBDatabaseParam.PageQuery pageQuery) {
        DataTable<OcCloudDbDatabase> table = ocCloudDBDatabaseService.fuzzyQueryOcCloudDBDatabaseByParam(pageQuery);
        List<CloudDBDatabaseVO.CloudDBDatabase> page = BeanCopierUtils.copyListProperties(table.getData(), CloudDBDatabaseVO.CloudDBDatabase.class);
        return new DataTable<>(page.stream().map(e -> cloudDBDatabaseDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> updateBaseCloudDBDatabase(CloudDBDatabaseVO.CloudDBDatabase cloudDBDatabase) {
        if (ocCloudDBDatabaseService.queryOcCloudDbDatabaseById(cloudDBDatabase.getId()) == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_DB_DATABASE_NOT_EXIST);
        OcCloudDbDatabase ocCloudDbDatabase = BeanCopierUtils.copyProperties(cloudDBDatabase, OcCloudDbDatabase.class);
        ocCloudDBDatabaseService.updateBaseOcCloudDbDatabase(ocCloudDbDatabase);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> privilegeAccount(CloudDBAccountVO.PrivilegeAccount privilegeAccount) {
        OcCloudDb ocCloudDb = ocCloudDBService.queryOcCloudDbById(privilegeAccount.getCloudDbId());
        if (ocCloudDb == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_DB_NOT_EXIST);
        List<OcCloudDbAccount> ocCloudDbAccountList = ocCloudDBAccountService.queryOcCloudDbAccountByCloudDbId(ocCloudDb.getId());
        Map<String, OcCloudDbAccount> accountMap = getOcCloudDbAccountMap(ocCloudDbAccountList);

        String key = CloudDBType.getName(ocCloudDb.getCloudDbType());
        ICloudDB iCloudDB = CloudDBFactory.getCloudDBByKey(key);
        // 新增账户
        for (String accountPrivilege : privilegeAccount.getPrivileges()) {
            if (!accountMap.containsKey(accountPrivilege)) {
                BusinessWrapper<Boolean> wrapper = createAccount(iCloudDB, ocCloudDb, accountPrivilege);
                if (!wrapper.isSuccess())
                    return wrapper;
                accountMap.remove(accountPrivilege);
            }
        }
        // 删除账户
        accountMap.keySet().forEach(k -> {
            OcCloudDbAccount ocCloudDbAccount = accountMap.get(k);
            BusinessWrapper<Boolean> wrapper = iCloudDB.deleteAccount(ocCloudDb, ocCloudDbAccount);
            if (wrapper.isSuccess())
                ocCloudDBAccountService.delOcCloudDbAccountById(ocCloudDbAccount.getId());
        });
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<CloudDatabaseSlowLogVO.SlowLog> queryCloudDBDatabaseSlowLogPage(CloudDBDatabaseParam.SlowLogPageQuery pageQuery) {
        OcCloudDb ocCloudDb = ocCloudDBService.queryOcCloudDbByUniqueKey(pageQuery.getCloudDbType(), pageQuery.getDbInstanceId());
        String key = CloudDBType.getName(ocCloudDb.getCloudDbType());
        ICloudDB iCloudDB = CloudDBFactory.getCloudDBByKey(key);
        return iCloudDB.querySlowLogPage(ocCloudDb, pageQuery);
    }

    private BusinessWrapper<Boolean> createAccount(ICloudDB iCloudDB, OcCloudDb ocCloudDb, String privilege) {
        OcCloudDbAccount ocCloudDbAccount = new OcCloudDbAccount();
        ocCloudDbAccount.setAccountName(Joiner.on("_").join(cloudDBConfig.getPrefix(), privilege.toLowerCase()));
        ocCloudDbAccount.setAccountType(cloudDBConfig.getType());
        String password = PasswordUtils.getPW(20); // 20位高强度密码
        ocCloudDbAccount.setAccountPassword(password);
        ocCloudDbAccount.setAccountPrivilege(privilege); // 权限
        ocCloudDbAccount.setCloudDbId(ocCloudDb.getId());
        ocCloudDbAccount.setDbInstanceId(ocCloudDb.getDbInstanceId());
        ocCloudDbAccount.setWorkflow(1);
        ocCloudDbAccount.setComment(Global.CREATED_BY);
        BusinessWrapper<Boolean> wrapper = iCloudDB.createAccount(ocCloudDb, ocCloudDbAccount, privilege);
        if (wrapper.isSuccess()) {
            // 加密
            ocCloudDbAccount.setAccountPassword(stringEncryptor.encrypt(password));
            ocCloudDBAccountService.addOcCloudDbAccount(ocCloudDbAccount);
        }
        return wrapper;
    }

    protected Map<String, OcCloudDbAccount> getOcCloudDbAccountMap(List<OcCloudDbAccount> ocCloudDbAccountList) {
        return ocCloudDbAccountList.stream().collect(Collectors.toMap(OcCloudDbAccount::getAccountPrivilege, a -> a, (k1, k2) -> k1));
    }

}
