package com.baiyi.opscloud.service.sys.impl;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.param.sys.CredentialParam;
import com.baiyi.opscloud.mapper.CredentialMapper;
import com.baiyi.opscloud.service.sys.CredentialService;
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
 * @Date 2021/5/17 3:35 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

    private final CredentialMapper credentialMapper;

    @Override
    public void add(Credential credential) {
        credentialMapper.insert(credential);
    }

    @Override
    public void update(Credential credential) {
        credentialMapper.updateByPrimaryKey(credential);
    }

    @Override
    public void updateBySelective(Credential credential) {
        credentialMapper.updateByPrimaryKeySelective(credential);
    }

    @Override
    public Credential getById(Integer id) {
        return credentialMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteById(Integer id) {
        credentialMapper.deleteByPrimaryKey(id);
    }

    @Override
    public DataTable<Credential> queryPageByParam(CredentialParam.CredentialPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(Credential.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            criteria.andLike("title", SQLUtil.toLike(pageQuery.getQueryName()));
        }
        if (!IdUtil.isEmpty(pageQuery.getKind())) {
            criteria.andEqualTo("kind", pageQuery.getKind());
        }
        example.setOrderByClause("kind, create_time");
        List<Credential> data = credentialMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

}