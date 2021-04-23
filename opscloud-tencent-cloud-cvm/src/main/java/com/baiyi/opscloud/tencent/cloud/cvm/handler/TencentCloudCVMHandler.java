package com.baiyi.opscloud.tencent.cloud.cvm.handler;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.tencent.cloud.cvm.base.BaseTencentCloudCVM;
import com.baiyi.opscloud.tencent.cloud.cvm.instance.CVMInstance;
import com.google.common.collect.Lists;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesResponse;
import com.tencentcloudapi.cvm.v20170312.models.Filter;
import com.tencentcloudapi.cvm.v20170312.models.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/20 4:10 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TencentCloudCVMHandler extends BaseTencentCloudCVM {

    /**
     * 查询所有CVM实例
     *
     * @return
     */
    public List<CVMInstance> getInstanceList() {
        try {
            // 实例化一个cvm实例信息查询请求对象,每个接口都会对应一个request对象。
            DescribeInstancesRequest req = new DescribeInstancesRequest();
            // 填充请求参数,这里request对象的成员变量即对应接口的入参
            // 你可以通过官网接口文档或跳转到request对象的定义处查看请求参数的定义
            Filter respFilter = new Filter(); // 创建Filter对象, 以zone的维度来查询cvm实例
            respFilter.setName("zone");
            String[] zones = new String[getZones().size()];
            respFilter.setValues(getZones().toArray(zones));
            // respFilter.setValues(new String[]{"ap-shanghai-1", "ap-shanghai-2"});
            req.setFilters(new Filter[]{respFilter}); // Filters 是成员为Filter对象的列表
            long size = QUERY_PAGE_SIZE;
            req.setOffset(0L);
            req.setLimit(QUERY_PAGE_SIZE - 1);
            List<Instance> instanceList = Lists.newArrayList();
            while (QUERY_PAGE_SIZE <= size) {
                DescribeInstancesResponse resp = cvmClient.DescribeInstances(req);
                size = resp.getTotalCount();
                instanceList.addAll(Arrays.asList(resp.getInstanceSet()));
                req.setOffset(req.getOffset() + QUERY_PAGE_SIZE); //  0 - 50
                req.setLimit(req.getLimit() + QUERY_PAGE_SIZE);  // 49 - 99
            }
           // return instanceList;
            return BeanCopierUtils.copyListProperties(instanceList, CVMInstance.class);
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 查询所有CVM实例
     *
     * @return
     */
    public CVMInstance getInstance(String instanceId) {
        try {
            // 实例化一个cvm实例信息查询请求对象,每个接口都会对应一个request对象。
            DescribeInstancesRequest req = new DescribeInstancesRequest();
            // 填充请求参数,这里request对象的成员变量即对应接口的入参
            // 你可以通过官网接口文档或跳转到request对象的定义处查看请求参数的定义
            Filter respFilter = new Filter(); // 创建Filter对象, 以zone的维度来查询cvm实例
            respFilter.set("instance-id", instanceId);
            req.setFilters(new Filter[]{respFilter}); // Filters 是成员为Filter对象的列表
            req.setOffset(0L);
            req.setLimit(1L);
            DescribeInstancesResponse resp = cvmClient.DescribeInstances(req);
            return BeanCopierUtils.copyProperties(resp.getInstanceSet()[0], CVMInstance.class);
        } catch (TencentCloudSDKException e) {
            return null;
        }
    }


}
