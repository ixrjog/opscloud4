package com.baiyi.opscloud.service.ser.impl;

import com.baiyi.opscloud.domain.generator.opscloud.SerDeploySubtaskCallback;
import com.baiyi.opscloud.mapper.SerDeploySubtaskCallbackMapper;
import com.baiyi.opscloud.service.ser.SerDeploySubtaskCallbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/7/5 7:34 PM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class SerDeploySubtaskCallbackServiceImpl implements SerDeploySubtaskCallbackService {

    private final SerDeploySubtaskCallbackMapper serDeploySubtaskCallbackMapper;

    @Override
    public void add(SerDeploySubtaskCallback serDeploySubtaskCallback) {
        serDeploySubtaskCallbackMapper.insert(serDeploySubtaskCallback);
    }

    @Override
    public List<SerDeploySubtaskCallback> listBySerDeploySubtaskId(Integer serDeploySubtaskId) {
        Example example = new Example(SerDeploySubtaskCallback.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serDeploySubtaskId", serDeploySubtaskId);
        return serDeploySubtaskCallbackMapper.selectByExample(example);
    }

    @Override
    public void deleteByBySerDeploySubtaskId(Integer serDeploySubtaskId) {
        Example example = new Example(SerDeploySubtaskCallback.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serDeploySubtaskId", serDeploySubtaskId);
        serDeploySubtaskCallbackMapper.deleteByExample(example);
    }

}