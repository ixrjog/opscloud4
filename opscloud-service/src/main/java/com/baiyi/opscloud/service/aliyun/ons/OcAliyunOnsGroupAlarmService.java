package com.baiyi.opscloud.service.aliyun.ons;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsGroupAlarm;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/13 4:20 下午
 * @Since 1.0
 */
public interface OcAliyunOnsGroupAlarmService {

    void addOcAliyunOnsGroupAlarm(OcAliyunOnsGroupAlarm ocAliyunOnsGroupAlarm);

    void updateOcAliyunOnsGroupAlarm(OcAliyunOnsGroupAlarm ocAliyunOnsGroupAlarm);

    void deleteOcAliyunOnsGroupAlarmById(int id);

    List<OcAliyunOnsGroupAlarm> queryOcAliyunOnsGroupAlarmByStatus(Integer alarmStatus);

    OcAliyunOnsGroupAlarm queryOcAliyunOnsGroupByOnsGroupId(Integer onsGroupId);
}
