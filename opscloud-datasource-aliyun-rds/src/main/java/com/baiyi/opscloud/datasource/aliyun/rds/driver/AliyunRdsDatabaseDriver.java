package com.baiyi.opscloud.datasource.aliyun.rds.driver;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.baiyi.opscloud.datasource.aliyun.rds.entity.AliyunRds;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.datasource.aliyun.core.SimpleAliyunClient.Query.PAGE_SIZE;

/**
 * @Author baiyi
 * @Date 2021/12/15 9:31 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunRdsDatabaseDriver {

    private final AliyunClient aliyunClient;

    /**
     * 查询数据库实例中的所有数据
     * https://help.aliyun.com/document_detail/26260.html
     *
     * @param regionId
     * @param aliyun
     * @param dbInstanceId
     * @return
     */
    public List<AliyunRds.Database> listDatabase(String regionId, AliyunConfig.Aliyun aliyun, String dbInstanceId) throws ClientException {
        DescribeDatabasesRequest describe = new DescribeDatabasesRequest();
        describe.setDBInstanceId(dbInstanceId);
        describe.setPageSize(PAGE_SIZE);
        int size = PAGE_SIZE;
        List<DescribeDatabasesResponse.Database> databases = Lists.newArrayList();
        int pageNumber = 1;
        // 返回值无总数，使用其它算法取所有数据库

        while (PAGE_SIZE <= size) {
            describe.setPageNumber(pageNumber);
            DescribeDatabasesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, describe);
            databases.addAll(response.getDatabases());
            size = response.getDatabases().size();
            pageNumber++;
        }
        return databases.stream().map(e -> {
            AliyunRds.Database database = BeanCopierUtil.copyProperties(e, AliyunRds.Database.class);
            database.setRegionId(regionId);
            return database;
        }).collect(Collectors.toList());
    }

}