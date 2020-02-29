package com.baiyi.opscloud.aliyun.rds.mysql.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

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
        DescribeDBInstancesResponse response = getInstancesResponse(describe, iAcsClient);
        instanceList.addAll(response.getItems());
        //cacheInstanceRenewAttribute(regionId, response);
        // 获取总数
        int totalCount = response.getTotalRecordCount();
        // 循环次数
        int cnt = (totalCount + QUERY_PAGE_SIZE - 1) / QUERY_PAGE_SIZE;
        for (int i = 1; i < cnt; i++) {
            describe.setPageNumber(i + 1);
            response = getInstancesResponse(describe, iAcsClient);
            instanceList.addAll(response.getItems());
        }
        return instanceList;
    }

    private DescribeDBInstancesResponse getInstancesResponse(DescribeDBInstancesRequest describe, IAcsClient client) {
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
