package com.baiyi.opscloud.datasource.aliyun.rds.mysql.drive;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.datasource.aliyun.core.SimpleAliyunClient.Query.PAGE_SIZE;

/**
 * @Author baiyi
 * @Date 2021/9/29 5:45 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunRdsMysqlDrive {

    private final AliyunClient aliyunClient;

    public static final String QUERY_ALL_INSTANCE = null;

    /**
     * 查询所有数据库实例
     *
     * @param regionId
     * @param aliyun
     * @return
     */
    public List<DescribeDBInstancesResponse.DBInstance> listDbInstance(String regionId, AliyunConfig.Aliyun aliyun) {
        return listDbInstance(regionId, aliyun, QUERY_ALL_INSTANCE);
    }

    /**
     * 查询数据库实例
     *
     * @param regionId
     * @param aliyun
     * @param dbInstanceId
     * @return
     */
    public List<DescribeDBInstancesResponse.DBInstance> listDbInstance(String regionId, AliyunConfig.Aliyun aliyun, String dbInstanceId) {
        List<DescribeDBInstancesResponse.DBInstance> instances = Lists.newArrayList();
        DescribeDBInstancesRequest describe = new DescribeDBInstancesRequest();
        if (!StringUtils.isEmpty(dbInstanceId))
            describe.setDBInstanceId(dbInstanceId);
        describe.setPageSize(PAGE_SIZE);
        int size = PAGE_SIZE;
        int pageNumber = 1;
        try {
            while (PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                DescribeDBInstancesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, describe);
                instances.addAll(response.getItems());
                size = response.getTotalRecordCount();
                pageNumber++;
            }
            return instances;
        } catch (ClientException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 查询数据库实例中的所有数据
     * https://help.aliyun.com/document_detail/26260.html
     * @param regionId
     * @param aliyun
     * @param dbInstanceId
     * @return
     */
    public List<DescribeDatabasesResponse.Database> listDatabase(String regionId, AliyunConfig.Aliyun aliyun, String dbInstanceId) {
        DescribeDatabasesRequest describe = new DescribeDatabasesRequest();
        describe.setDBInstanceId(dbInstanceId);
        describe.setPageSize(PAGE_SIZE);
        int size = PAGE_SIZE;
        List<DescribeDatabasesResponse.Database> databases = Lists.newArrayList();
        int pageNumber = 1;
        // 返回值无总数，使用其它算法取所有数据库
        try {
            while (PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                DescribeDatabasesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, describe);
                databases.addAll(response.getDatabases());
                size = response.getDatabases().size();
                pageNumber++;
            }
            return databases;
        } catch (ClientException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

}
