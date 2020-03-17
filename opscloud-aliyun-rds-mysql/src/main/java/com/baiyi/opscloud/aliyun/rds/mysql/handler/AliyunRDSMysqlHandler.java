package com.baiyi.opscloud.aliyun.rds.mysql.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.rds.model.v20140815.*;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbAccount;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/28 5:35 下午
 * @Version 1.0
 */
@Component
public class AliyunRDSMysqlHandler {

    @Resource
    private AliyunCore aliyunCore;

    public static final int QUERY_PAGE_SIZE = 50; // 默默值30 最大值100

    public BusinessWrapper<Boolean> deleteAccount(AliyunAccount aliyunAccount, String dbInstanceId, String accountName) {
        DeleteAccountRequest request = new DeleteAccountRequest();
        request.setDBInstanceId(dbInstanceId);
        request.setAccountName(accountName);
        IAcsClient client = acqAcsClient(aliyunAccount.getRegionId(), aliyunAccount);
        try {
            DeleteAccountResponse response = client.getAcsResponse(request);
            if (!StringUtils.isEmpty(response.getRequestId()))
                return BusinessWrapper.SUCCESS;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new BusinessWrapper(ErrorEnum.ALIYUN_RDS_MYSQL_DELETE_ACCOUNT_ERROR); // 授权错误
    }

    /**
     * 撤销账号对数据库的访问权限
     *
     * @param aliyunAccount
     * @param ocCloudDbAccount
     * @param dbName
     * @return
     */
    public BusinessWrapper<Boolean> revokeAccountPrivilege(AliyunAccount aliyunAccount, OcCloudDbAccount ocCloudDbAccount, String dbName) {
        RevokeAccountPrivilegeRequest request = new RevokeAccountPrivilegeRequest();
        request.setAccountName(ocCloudDbAccount.getAccountName());
        request.setDBInstanceId(ocCloudDbAccount.getDbInstanceId());
        request.setDBName(dbName);
        IAcsClient client = acqAcsClient(aliyunAccount.getRegionId(), aliyunAccount);
        try {
            RevokeAccountPrivilegeResponse response = client.getAcsResponse(request);
            if (!StringUtils.isEmpty(response.getRequestId()))
                return BusinessWrapper.SUCCESS;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new BusinessWrapper(ErrorEnum.ALIYUN_RDS_MYSQL_REVOKE_ACCOUNT_PRIVILEGE_ERROR); // 授权错误
    }

    /**
     * 授权账号访问数据库
     *
     * @param aliyunAccount
     * @param ocCloudDbAccount
     * @param dbName
     * @return
     */
    public BusinessWrapper<Boolean> grantAccountPrivilege(AliyunAccount aliyunAccount, OcCloudDbAccount ocCloudDbAccount, String dbName) {
        GrantAccountPrivilegeRequest request = new GrantAccountPrivilegeRequest();
        request.setAccountName(ocCloudDbAccount.getAccountName());
        request.setAccountPrivilege(ocCloudDbAccount.getAccountPrivilege());
        request.setDBInstanceId(ocCloudDbAccount.getDbInstanceId());
        request.setDBName(dbName);
        IAcsClient client = acqAcsClient(aliyunAccount.getRegionId(), aliyunAccount);
        try {
            GrantAccountPrivilegeResponse response = client.getAcsResponse(request);
            if (!StringUtils.isEmpty(response.getRequestId()))
                return BusinessWrapper.SUCCESS;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new BusinessWrapper(ErrorEnum.ALIYUN_RDS_MYSQL_GRANT_ACCOUNT_PRIVILEGE_ERROR); // 授权错误
    }

    /**
     * 创建账户
     *
     * @param aliyunAccount
     * @param ocCloudDbAccount
     * @return
     */
    public BusinessWrapper<Boolean> createAccount(AliyunAccount aliyunAccount, OcCloudDbAccount ocCloudDbAccount) {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setAccountName(ocCloudDbAccount.getAccountName());
        request.setAccountPassword(ocCloudDbAccount.getAccountPassword());
        request.setDBInstanceId(ocCloudDbAccount.getDbInstanceId());
        request.setAccountDescription(ocCloudDbAccount.getComment());
        request.setAccountType(ocCloudDbAccount.getAccountType());
        IAcsClient client = acqAcsClient(aliyunAccount.getRegionId(), aliyunAccount);
        try {
            CreateAccountResponse response = client.getAcsResponse(request);
            if (!StringUtils.isEmpty(response.getRequestId()))
                return BusinessWrapper.SUCCESS;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new BusinessWrapper(ErrorEnum.ALIYUN_RDS_MYSQL_CREATE_ACCOUNT_ERROR); // 创建账户错误
    }

    /**
     * 查询单个账户详情
     *
     * @param aliyunAccount
     * @param ocCloudDbAccount
     */
    public DescribeAccountsResponse.DBInstanceAccount getAccount(AliyunAccount aliyunAccount, OcCloudDbAccount ocCloudDbAccount) {
        DescribeAccountsRequest request = new DescribeAccountsRequest();
        request.setDBInstanceId(ocCloudDbAccount.getDbInstanceId());
        request.setAccountName(ocCloudDbAccount.getAccountName());
        IAcsClient client = acqAcsClient(aliyunAccount.getRegionId(), aliyunAccount);
        try {
            DescribeAccountsResponse response = client.getAcsResponse(request);
            if (response.getAccounts() == null || response.getAccounts().size() == 0)
                return null;
            return response.getAccounts().get(0);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DescribeDatabasesResponse.Database> getDatabaseList(AliyunAccount aliyunAccount, String dbInstanceId) {
        DescribeDatabasesRequest describe = new DescribeDatabasesRequest();
        describe.setDBInstanceId(dbInstanceId);
        describe.setPageSize(QUERY_PAGE_SIZE);
        IAcsClient client = acqAcsClient(aliyunAccount.getRegionId(), aliyunAccount);
        int size = QUERY_PAGE_SIZE;
        List<DescribeDatabasesResponse.Database> databaseList = Lists.newArrayList();
        int pageNumber = 1;
        // 返回值无总数，使用其它算法取所有数据库
        while (QUERY_PAGE_SIZE <= size) {
            describe.setPageNumber(pageNumber);
            DescribeDatabasesResponse response = describeDatabasesResponse(describe, client);
            databaseList.addAll(response.getDatabases());
            size = response.getDatabases().size();
            pageNumber++;
        }
        return databaseList;
    }

    private DescribeDatabasesResponse describeDatabasesResponse(DescribeDatabasesRequest describe, IAcsClient client) {
        try {
            DescribeDatabasesResponse response = client.getAcsResponse(describe);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DescribeDBInstancesResponse.DBInstance> getDbInstanceList(String regionId, AliyunAccount aliyunAccount) {
        IAcsClient iAcsClient = acqAcsClient(regionId, aliyunAccount);
        return getInstanceList(iAcsClient);
    }

    // DescribeDBInstanceAttribute
    public List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute> getDbInstanceAttribute(AliyunAccount aliyunAccount, String dbInstanceId) {
        DescribeDBInstanceAttributeRequest describe = new DescribeDBInstanceAttributeRequest();
        describe.setDBInstanceId(dbInstanceId);
        describe.setExpired("False"); // 实例过期状态(未过期)
        try {
            IAcsClient client = acqAcsClient(aliyunAccount.getRegionId(), aliyunAccount);
            DescribeDBInstanceAttributeResponse response = client.getAcsResponse(describe);
            return response.getItems();
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<DescribeDBInstancesResponse.DBInstance> getInstanceList(IAcsClient iAcsClient) {
        List<DescribeDBInstancesResponse.DBInstance> instanceList = Lists.newArrayList();
        DescribeDBInstancesRequest describe = new DescribeDBInstancesRequest();
        describe.setPageSize(QUERY_PAGE_SIZE);
        DescribeDBInstancesResponse response = describeDBInstancesResponse(describe, iAcsClient);
        instanceList.addAll(response.getItems());
        //cacheInstanceRenewAttribute(regionId, response);
        // 获取总数
        int totalCount = response.getTotalRecordCount();
        // 循环次数
        int cnt = (totalCount + QUERY_PAGE_SIZE - 1) / QUERY_PAGE_SIZE;
        for (int i = 1; i < cnt; i++) {
            describe.setPageNumber(i + 1);
            response = describeDBInstancesResponse(describe, iAcsClient);
            instanceList.addAll(response.getItems());
        }
        return instanceList;
    }

    private DescribeDBInstancesResponse describeDBInstancesResponse(DescribeDBInstancesRequest describe, IAcsClient client) {
        try {
            DescribeDBInstancesResponse response = client.getAcsResponse(describe);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    private IAcsClient acqAcsClient(String regionId, AliyunAccount aliyunAccount) {
        return aliyunCore.getAcsClient(regionId, aliyunAccount);
    }
}
