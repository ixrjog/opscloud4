package com.baiyi.opscloud.datasource.aliyun.rds.driver;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.baiyi.opscloud.datasource.aliyun.rds.entity.AliyunRds;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.datasource.aliyun.core.SimpleAliyunClient.Query.RDS_INSTANCE_PAGE_SIZE;

/**
 * @Author baiyi
 * @Date 2021/9/29 5:45 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunRdsInstanceDriver {

    private final AliyunClient aliyunClient;

    public static final String QUERY_ALL_INSTANCE = null;

    /**
     * 查询所有数据库实例
     *
     * @param regionId
     * @param aliyun
     * @return
     */
    public List<AliyunRds.DBInstanceAttribute> listDbInstance(String regionId, AliyunConfig.Aliyun aliyun) throws ClientException {
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
    public List<AliyunRds.DBInstanceAttribute> listDbInstance(String regionId, AliyunConfig.Aliyun aliyun, String dbInstanceId) throws ClientException {
        List<AliyunRds.DBInstanceAttribute> instances = Lists.newArrayList();
        DescribeDBInstancesRequest describe = new DescribeDBInstancesRequest();
        if (!StringUtils.isEmpty(dbInstanceId))
            describe.setDBInstanceId(dbInstanceId);
        describe.setPageSize(RDS_INSTANCE_PAGE_SIZE);
        int size = RDS_INSTANCE_PAGE_SIZE;
        int pageNumber = 1;
        while (RDS_INSTANCE_PAGE_SIZE <= size) {
            describe.setPageNumber(pageNumber);
            DescribeDBInstancesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, describe);
            instances.addAll(getDbInstance(regionId,aliyun,response.getItems()));
            size = response.getTotalRecordCount();
            pageNumber++;
        }
        return instances;
    }

    public List<AliyunRds.DBInstanceAttribute> getDbInstance(String regionId, AliyunConfig.Aliyun aliyun, List<DescribeDBInstancesResponse.DBInstance> instances) throws ClientException {
        Iterator<DescribeDBInstancesResponse.DBInstance> iter = instances.iterator();
        List<String> ids = instances.stream().map(DescribeDBInstancesResponse.DBInstance::getDBInstanceId).collect(Collectors.toList());
        DescribeDBInstanceAttributeRequest request = new DescribeDBInstanceAttributeRequest();
        request.setDBInstanceId(Joiner.on(",").join(ids));

        DescribeDBInstanceAttributeResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        return response.getItems().stream().map(e ->
                BeanCopierUtil.copyProperties(e, AliyunRds.DBInstanceAttribute.class)
        ).collect(Collectors.toList());
    }

}