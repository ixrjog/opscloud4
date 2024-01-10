package com.baiyi.opscloud.service.ser.impl;

import com.baiyi.opscloud.domain.generator.opscloud.SerDeploySubtask;
import com.baiyi.opscloud.mapper.SerDeploySubtaskMapper;
import com.baiyi.opscloud.service.ser.SerDeploySubtaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/6/7 10:59 AM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class SerDeploySubtaskServiceImpl implements SerDeploySubtaskService {

    private final SerDeploySubtaskMapper serDeploySubtaskMapper;

    @Override
    public void add(SerDeploySubtask serDeploySubtask) {
        serDeploySubtaskMapper.insert(serDeploySubtask);
    }

    @Override
    public void update(SerDeploySubtask serDeploySubtask) {
        serDeploySubtaskMapper.updateByPrimaryKey(serDeploySubtask);
    }

    @Override
    public SerDeploySubtask getById(Integer id) {
        return serDeploySubtaskMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SerDeploySubtask> listBySerDeployTaskId(Integer serDeployTaskId) {
        Example example = new Example(SerDeploySubtask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serDeployTaskId", serDeployTaskId);
        return serDeploySubtaskMapper.selectByExample(example);
    }

    @Override
    public SerDeploySubtask getByTaskIdAndEnvType(Integer serDeployTaskId, Integer envType) {
        Example example = new Example(SerDeploySubtask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serDeployTaskId", serDeployTaskId)
                .andEqualTo("envType", envType);
        return serDeploySubtaskMapper.selectOneByExample(example);
    }

}