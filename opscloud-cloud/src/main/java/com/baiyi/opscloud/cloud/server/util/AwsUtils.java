package com.baiyi.opscloud.cloud.server.util;

import com.amazonaws.services.ec2.model.Tag;
import com.baiyi.opscloud.aws.ec2.base.EC2Volume;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/13 12:52 下午
 * @Version 1.0
 */
public class AwsUtils {

    public static String getEC2InstanceName(com.amazonaws.services.ec2.model.Instance instance) {
        List<Tag> tags = instance.getTags();
        for (Tag tag : tags) {
            if (tag.getKey().equals("Name")) {
                return tag.getValue();
            }
        }
        return "";
    }

    public static Map<String, Integer> getEC2VolumeSizeMap(List<EC2Volume> volumeList) {
        int dataDiskSize = 0;
        int systemDiskSize = 0;
        for (EC2Volume volume : volumeList) {
            if (volume.getIsRootDevice()) {
                systemDiskSize = volume.getSize();
            } else {
                dataDiskSize += volume.getSize();
            }
        }
        Map<String, Integer> map = Maps.newHashMap();
        map.put("systemDiskSize", systemDiskSize);
        map.put("dataDiskSize", dataDiskSize);
        return map;
    }

}
