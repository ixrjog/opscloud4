package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerTask;
import com.baiyi.opscloud.domain.param.ansible.ServerTaskHistoryParam;
import com.baiyi.opscloud.mapper.opscloud.OcServerTaskMapper;
import com.baiyi.opscloud.service.server.OcServerTaskService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/7 9:09 下午
 * @Version 1.0
 */
@Service
public class OcServerTaskServiceImpl implements OcServerTaskService {

    @Resource
    private OcServerTaskMapper ocServerTaskMapper;

    @Override
    public DataTable<OcServerTask> queryOcServerTaskByParam(ServerTaskHistoryParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcServerTask> taskList = ocServerTaskMapper.queryOcServerTaskByParam(pageQuery);
        return new DataTable<>(taskList, page.getTotal());
    }

    @Override
    public void addOcServerTask(OcServerTask ocServerTask) {
        ocServerTaskMapper.insert(ocServerTask);
    }

    @Override
    public void updateOcServerTask(OcServerTask ocServerTask) {
        ocServerTaskMapper.updateByPrimaryKey(ocServerTask);
    }

    @Override
    public OcServerTask queryOcServerTaskById(int id) {
        return ocServerTaskMapper.selectByPrimaryKey(id);
    }

}
