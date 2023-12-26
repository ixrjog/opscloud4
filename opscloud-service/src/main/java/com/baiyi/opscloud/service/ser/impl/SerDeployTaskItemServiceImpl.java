package com.baiyi.opscloud.service.ser.impl;

import com.baiyi.opscloud.domain.generator.opscloud.SerDeployTaskItem;
import com.baiyi.opscloud.mapper.SerDeployTaskItemMapper;
import com.baiyi.opscloud.service.ser.SerDeployTaskItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/6/7 11:00 AM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class SerDeployTaskItemServiceImpl implements SerDeployTaskItemService {

    private final SerDeployTaskItemMapper serDeployTaskItemMapper;

    @Override
    public void add(SerDeployTaskItem serDeployTaskItem) {
        serDeployTaskItemMapper.insert(serDeployTaskItem);
    }

    @Override
    public void update(SerDeployTaskItem serDeployTaskItem) {
        serDeployTaskItemMapper.updateByPrimaryKey(serDeployTaskItem);
    }

    @Override
    public SerDeployTaskItem getById(Integer id) {
        return serDeployTaskItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteById(Integer id) {
        serDeployTaskItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<SerDeployTaskItem> listBySerDeployTaskId(Integer serDeployTaskId) {
        Example example = new Example(SerDeployTaskItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serDeployTaskId", serDeployTaskId);
        return serDeployTaskItemMapper.selectByExample(example);
    }

    @Override
    public SerDeployTaskItem getByTaskIdAndItemName(Integer serDeployTaskId, String itemName) {
        Example example = new Example(SerDeployTaskItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serDeployTaskId", serDeployTaskId);
        criteria.andEqualTo("itemName", itemName);
        return serDeployTaskItemMapper.selectOneByExample(example);
    }

}