package com.baiyi.opscloud.service.aliyun.ons;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsInstance;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/5 2:17 下午
 * @Since 1.0
 */
public interface OcAliyunOnsInstanceService {

    void addOcAliyunOnsInstance(OcAliyunOnsInstance ocAliyunOnsInstance);

    void updateOcAliyunOnsInstance(OcAliyunOnsInstance ocAliyunOnsInstance);

    void deleteOcAliyunOnsInstanceById(int id);

    List<OcAliyunOnsInstance> queryOcAliyunOnsInstanceByRegionId(String regionId);

    OcAliyunOnsInstance queryOcAliyunOnsInstanceByInstanceId(String instanceId);

    List<OcAliyunOnsInstance> queryOcAliyunOnsInstanceAll();

    int countOcAliyunOnsInstance();

    List<OcAliyunOnsInstance> queryOcAliyunOnsInstanceByInstanceIdList(List<String> instanceIdList);

    OcAliyunOnsInstance queryOcAliyunOnsInstance(int id);
}
