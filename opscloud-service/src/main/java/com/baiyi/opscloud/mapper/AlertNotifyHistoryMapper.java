package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyHistory;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface AlertNotifyHistoryMapper extends Mapper<AlertNotifyHistory>, InsertListMapper<AlertNotifyHistory> {
}