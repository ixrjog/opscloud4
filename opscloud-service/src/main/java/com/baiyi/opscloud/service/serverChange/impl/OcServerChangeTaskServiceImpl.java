package com.baiyi.opscloud.service.serverChange.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.mapper.opscloud.OcServerChangeTaskMapper;
import com.baiyi.opscloud.service.serverChange.OcServerChangeTaskService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/27 1:48 下午
 * @Version 1.0
 */
@Service
public class OcServerChangeTaskServiceImpl implements OcServerChangeTaskService {


    @Resource
    private OcServerChangeTaskMapper ocServerChangeTaskMapper;

    @Override
    public void addOcServerChangeTask(OcServerChangeTask ocServerChangeTask) {
        ocServerChangeTaskMapper.insert(ocServerChangeTask);
    }

    @Override
    public void updateOcServerChangeTask(OcServerChangeTask ocServerChangeTask) {
        ocServerChangeTaskMapper.updateByPrimaryKey(ocServerChangeTask);
    }

    @Override
    public OcServerChangeTask checkOcServerChangeTask(OcServerChangeTask ocServerChangeTask) {
        Example example = new Example(OcServerChangeTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverId", ocServerChangeTask.getServerId());
        criteria.andEqualTo("taskStatus", ocServerChangeTask.getTaskStatus()); // 正在运行的任务
        PageHelper.startPage(1, 1); // limit 1
        return ocServerChangeTaskMapper.selectOneByExample(example);
    }

    @Override
    public OcServerChangeTask queryOcServerChangeTaskByTaskId(String taskId) {
        Example example = new Example(OcServerChangeTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
        PageHelper.startPage(1, 1);
        return ocServerChangeTaskMapper.selectOneByExample(example);
    }

    @Override
    public List<OcServerChangeTask> queryOcServerChangeTaskByTaskStatus(Integer taskStatus) {
        Example example = new Example(OcServerChangeTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskStatus", taskStatus);
        return ocServerChangeTaskMapper.selectByExample(example);
    }
}
