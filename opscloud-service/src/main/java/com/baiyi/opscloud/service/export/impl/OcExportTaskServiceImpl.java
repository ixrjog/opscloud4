package com.baiyi.opscloud.service.export.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcExportTask;
import com.baiyi.opscloud.domain.param.export.ExportParam;
import com.baiyi.opscloud.mapper.opscloud.OcExportTaskMapper;
import com.baiyi.opscloud.service.export.OcExportTaskService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/4 11:44 上午
 * @Since 1.0
 */

@Service
public class OcExportTaskServiceImpl implements OcExportTaskService {

    @Resource
    private OcExportTaskMapper ocExportTaskMapper;

    @Override
    public void addOcExportTask(OcExportTask ocExportTask) {
        ocExportTaskMapper.insert(ocExportTask);
    }

    @Override
    public void updateOcExportTask(OcExportTask ocExportTask) {
        ocExportTaskMapper.updateByPrimaryKey(ocExportTask);
    }

    @Override
    public DataTable<OcExportTask> queryOcExportTaskPage(ExportParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(OcExportTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", pageQuery.getUsername());
        criteria.andEqualTo("taskType", pageQuery.getTaskType());
        example.setOrderByClause(" start_time DESC");
        List<OcExportTask> taskList = ocExportTaskMapper.selectByExample(example);
        return new DataTable<>(taskList, page.getTotal());
    }

    @Override
    public List<OcExportTask> queryOcExportTaskByType(String username, Integer taskType) {
        Example example = new Example(OcExportTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskStatus", 1);
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("taskType", taskType);
        return ocExportTaskMapper.selectByExample(example);
    }
}
