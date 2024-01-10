package com.baiyi.opscloud.service.alert.impl;

import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyHistory;
import com.baiyi.opscloud.mapper.AlertNotifyHistoryMapper;
import com.baiyi.opscloud.service.alert.AlertNotifyHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
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

    @Override
    public List<AlertNotifyHistory> listByEventId(Integer alertNotifyEventId) {
        Example example = new Example(AlertNotifyHistory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("alertNotifyEventId", alertNotifyEventId);
        return alertNotifyHistoryMapper.selectByExample(example);
    }

    @Override
    public List<AlertNotifyHistory> listByAlertTime(Date alertTime) {
        Example example = new Example(AlertNotifyHistory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("alertTime", alertTime);
        return alertNotifyHistoryMapper.selectByExample(example);
    }

    @Override
    public void delete(Integer id) {
        alertNotifyHistoryMapper.deleteByPrimaryKey(id);
    }

}