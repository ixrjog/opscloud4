package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AuthPlatformLog;
import com.baiyi.opscloud.domain.param.auth.AuthPlatformParam;
import com.baiyi.opscloud.mapper.AuthPlatformLogMapper;
import com.baiyi.opscloud.service.auth.AuthPlatformLogService;
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
 * @Date 2022/8/22 14:47
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthPlatformLogServiceImpl implements AuthPlatformLogService {

    private final AuthPlatformLogMapper authPlatformLogMapper;

    @Override
    public void add(AuthPlatformLog authPlatformLog) {
        authPlatformLogMapper.insert(authPlatformLog);
    }

    @Override
    public DataTable<AuthPlatformLog> queryPageByParam(AuthPlatformParam.AuthPlatformLogPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(AuthPlatformLog.class);
        Example.Criteria criteria = example.createCriteria();
        if (IdUtil.isNotEmpty(pageQuery.getPlatformId())) {
            criteria.andEqualTo("platformId", pageQuery.getPlatformId());
        }
        if (StringUtils.isNotBlank(pageQuery.getUsername())) {
            criteria.andLike("username", SQLUtil.toLike(pageQuery.getUsername()));
        }
        if (pageQuery.getResult() != null) {
            criteria.andEqualTo("result", pageQuery.getResult());
        }
        example.setOrderByClause("create_time DESC");
        List<AuthPlatformLog> data= authPlatformLogMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

}