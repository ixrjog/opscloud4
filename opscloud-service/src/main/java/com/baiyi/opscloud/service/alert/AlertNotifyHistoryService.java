package com.baiyi.opscloud.service.alert;

import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyHistory;

import java.util.Date;
import java.util.List;

/**
 * @Author 修远
 * @Date 2022/7/29 6:03 PM
 * @Since 1.0
 */
public interface AlertNotifyHistoryService {

    void add(AlertNotifyHistory alertNotifyHistory);

    void update(AlertNotifyHistory alertNotifyHistory);

    void addList(List<AlertNotifyHistory> alertNotifyHistoryList);

    List<AlertNotifyHistory> listByEventId(Integer alertNotifyEventId);

    List<AlertNotifyHistory> listByAlertTime(Date alertTime);

    void delete(Integer id);

}