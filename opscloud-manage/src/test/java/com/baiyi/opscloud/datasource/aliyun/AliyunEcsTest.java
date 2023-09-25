package com.baiyi.opscloud.datasource.aliyun;

import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.ecs.model.v20140526.TagResourcesRequest;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.base.BaseAliyunTest;
import com.baiyi.opscloud.datasource.aliyun.ecs.driver.AliyunEcsDriver;
import com.baiyi.opscloud.datasource.aliyun.ecs.driver.AliyunTagDriver;
import jakarta.annotation.Resource;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/9/25 10:47
 * @Version 1.0
 */
public class AliyunEcsTest extends BaseAliyunTest {

    @Resource
    private AliyunEcsDriver aliyunEcsDriver;

    @Resource
    private AliyunTagDriver aliyunTagDriver;

    /**
     * - eu-west-1
     * - eu-central-1
     * - cn-hangzhou
     * - cn-shenzhen
     * - cn-hongkong
     */
    @Test
    void finOpsTagResourcesTest() {
        final String regionId = "eu-central-1";
        AliyunConfig config = getConfigById(1);
        List<DescribeInstancesResponse.Instance> instanceList = aliyunEcsDriver.listInstances(regionId, config.getAliyun());
        for (DescribeInstancesResponse.Instance instance : instanceList) {
            List<DescribeInstancesResponse.Instance.Tag> tags = instance.getTags();
            if (CollectionUtils.isEmpty(tags)) {
                continue;
            }
            Optional<DescribeInstancesResponse.Instance.Tag> optionalTag = tags.stream().filter(t -> t.getTagKey().equals("FIN")).findFirst();
            if (optionalTag.isPresent()) {
                final String tagValue = optionalTag.get().getTagValue();
                List<DescribeDisksResponse.Disk> disks = aliyunEcsDriver.describeDisks(instance.getRegionId(), config.getAliyun(), instance.getInstanceId());
                // print(disks);
                if (!CollectionUtils.isEmpty(disks)) {
                    List<String> resourceIds = disks.stream().map(DescribeDisksResponse.Disk::getDiskId).toList();
                    TagResourcesRequest.Tag tag = new TagResourcesRequest.Tag();
                    tag.setKey("FIN");
                    tag.setValue(tagValue);
                    aliyunTagDriver.tagResources(instance.getRegionId(), config.getAliyun(), "disk", resourceIds, Lists.newArrayList(tag));
                }

            }
        }
    }

}
