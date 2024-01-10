package com.baiyi.opscloud.service.datasource.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AliyunLog;
import com.baiyi.opscloud.domain.param.datasource.AliyunLogParam;
import com.baiyi.opscloud.mapper.AliyunLogMapper;
import com.baiyi.opscloud.service.datasource.AliyunLogService;
import com.baiyi.opscloud.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/16 5:44 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AliyunLogServiceImpl implements AliyunLogService {

    private final AliyunLogMapper aliyunLogMapper;

    @Override
    public DataTable<AliyunLog> queryAliyunLogByParam(AliyunLogParam.AliyunLogPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(AliyunLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("datasourceInstanceId", pageQuery.getInstanceId());
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            criteria.andLike("comment", SQLUtil.toLike(pageQuery.getQueryName()));
        }
        List<AliyunLog> data = aliyunLogMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public void add(AliyunLog aliyunLog) {
        aliyunLogMapper.insert(aliyunLog);
    }

    @Override
    public void update(AliyunLog aliyunLog) {
        aliyunLogMapper.updateByPrimaryKey(aliyunLog);
    }

    @Override
    public void deleteById(Integer id) {
        aliyunLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public AliyunLog getById(Integer id) {
        return aliyunLogMapper.selectByPrimaryKey(id);
    }

}