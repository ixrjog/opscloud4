package com.baiyi.opscloud.service.datasource.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AliyunLogMember;
import com.baiyi.opscloud.domain.param.datasource.AliyunLogMemberParam;
import com.baiyi.opscloud.mapper.AliyunLogMemberMapper;
import com.baiyi.opscloud.service.datasource.AliyunLogMemberService;
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
 * @Date 2021/9/16 5:50 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AliyunLogMemberServiceImpl implements AliyunLogMemberService {

    private final AliyunLogMemberMapper aliyunLogMemberMapper;

    @Override
    public DataTable<AliyunLogMember> queryAliyunLogMemberByParam(AliyunLogMemberParam.LogMemberPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(AliyunLogMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("aliyunLogId", pageQuery.getAliyunLogId());
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            Example.Criteria criteria2 = example.createCriteria();
            String likeName = SQLUtil.toLike(pageQuery.getQueryName());
            criteria2.orLike("serverGroupName", likeName)
                    .orLike("topic", likeName)
                    .orLike("comment", likeName);
            example.and(criteria2);
        }
        example.setOrderByClause("create_time");
        List<AliyunLogMember> data = aliyunLogMemberMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<AliyunLogMember> queryByServerGroupId(Integer serverGroupId) {
        Example example = new Example(AliyunLogMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId",serverGroupId);
        return aliyunLogMemberMapper.selectByExample(example);
    }

    @Override
    public int countByAliyunLogId(Integer aliyunLogId) {
        Example example = new Example(AliyunLogMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("aliyunLogId", aliyunLogId);
        return aliyunLogMemberMapper.selectCountByExample(example);
    }

    @Override
    public List<AliyunLogMember> queryByAliyunLogId(Integer aliyunLogId) {
        Example example = new Example(AliyunLogMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("aliyunLogId", aliyunLogId);
        return aliyunLogMemberMapper.selectByExample(example);
    }

    @Override
    public void deleteById(Integer id) {
        aliyunLogMemberMapper.deleteByPrimaryKey(id);
    }

    @Override
    public AliyunLogMember getById(Integer id) {
        return aliyunLogMemberMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(AliyunLogMember aliyunLogMember) {
        aliyunLogMemberMapper.updateByPrimaryKey(aliyunLogMember);
    }

    @Override
    public void add(AliyunLogMember aliyunLogMember) {
        aliyunLogMemberMapper.insert(aliyunLogMember);
    }

}