package com.baiyi.opscloud.service.application.impl;

import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResourceOperationLog;
import com.baiyi.opscloud.mapper.opscloud.ApplicationResourceOperationLogMapper;
import com.baiyi.opscloud.service.application.ApplicationResourceOperationLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/5 13:05
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ApplicationResourceOperationLogServiceImpl implements ApplicationResourceOperationLogService {

    private final ApplicationResourceOperationLogMapper applicationResourceOperationLogMapper;

    @Override
    public List<ApplicationResourceOperationLog> queryByResourceId(Integer resourceId,int limit){
        Page page = PageHelper.startPage(1, limit);
        Example example = new Example(ApplicationResourceOperationLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("resourceId", resourceId);
        example.setOrderByClause("operation_time DESC");
        return applicationResourceOperationLogMapper.selectByExample(example);
    }

    @Override
    public void add(ApplicationResourceOperationLog applicationResourceOperationLog) {
        applicationResourceOperationLogMapper.insert(applicationResourceOperationLog);
    }

    @Override
    public void update(ApplicationResourceOperationLog applicationResourceOperationLog) {
        applicationResourceOperationLogMapper.updateByPrimaryKey(applicationResourceOperationLog);
    }

    @Override
    public void updateByPrimaryKeySelective(ApplicationResourceOperationLog applicationResourceOperationLog) {
        applicationResourceOperationLogMapper.updateByPrimaryKeySelective(applicationResourceOperationLog);
    }

}
