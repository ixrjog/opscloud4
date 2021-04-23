package com.baiyi.opscloud.service.aliyun.ons;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsGroup;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 8:17 下午
 * @Since 1.0
 */
public interface OcAliyunOnsGroupService {

    OcAliyunOnsGroup queryOcAliyunOnsGroupById(Integer id);

    void addOcAliyunOnsGroup(OcAliyunOnsGroup ocAliyunOnsGroup);

    void updateOcAliyunOnsGroup(OcAliyunOnsGroup ocAliyunOnsGroup);

    void deleteOcAliyunOnsGroupById(int id);

    DataTable<OcAliyunOnsGroup> queryONSGroupPage(AliyunONSParam.GroupPageQuery pageQuery);

    List<OcAliyunOnsGroup> queryOcAliyunOnsGroupByInstanceId(String instanceId);

    OcAliyunOnsGroup queryOcAliyunOnsGroupByInstanceIdAndGroupId(String instanceId, String groupId);

    int countOcAliyunOnsGroupByInstanceId(String instanceId);

    int countOcAliyunOnsGroup();

    List<OcAliyunOnsGroup> queryOcAliyunOnsGroupByGroupId(String groupId);
}
