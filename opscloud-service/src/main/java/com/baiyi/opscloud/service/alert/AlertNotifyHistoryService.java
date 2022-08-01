package com.baiyi.opscloud.service.alert;

import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyHistory;

/**
 * @Author 修远
 * @Date 2022/7/29 6:03 PM
 * @Since 1.0
 */
public interface AlertNotifyHistoryService {

    void add(AlertNotifyHistory alertNotifyHistory);

}
