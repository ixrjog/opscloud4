package com.baiyi.opscloud.service.alert.impl;

import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyHistory;
import com.baiyi.opscloud.mapper.opscloud.AlertNotifyHistoryMapper;
import com.baiyi.opscloud.service.alert.AlertNotifyHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/7/29 6:03 PM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class AlertNotifyHistoryServiceImpl implements AlertNotifyHistoryService {

    private final AlertNotifyHistoryMapper alertNotifyHistoryMapper;

    @Override
    public void add(AlertNotifyHistory alertNotifyHistory) {
        alertNotifyHistoryMapper.insert(alertNotifyHistory);
    }

    @Override
    public void addList(List<AlertNotifyHistory> alertNotifyHistoryList) {
        alertNotifyHistoryMapper.insertList(alertNotifyHistoryList);
    }

    @Override
    public void update(AlertNotifyHistory alertNotifyHistory) {
        alertNotifyHistoryMapper.updateByPrimaryKey(alertNotifyHistory);
    }
}
