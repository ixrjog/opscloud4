package com.baiyi.opscloud.service.ansible.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AnsiblePlaybook;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.opscloud.mapper.AnsiblePlaybookMapper;
import com.baiyi.opscloud.service.ansible.AnsiblePlaybookService;
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
 * @Date 2021/8/31 5:59 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AnsiblePlaybookServiceImpl implements AnsiblePlaybookService {

    private final AnsiblePlaybookMapper ansiblePlaybookMapper;

    @Override
    public void add(AnsiblePlaybook ansiblePlaybook) {
        ansiblePlaybookMapper.insert(ansiblePlaybook);
    }

    @Override
    public void update(AnsiblePlaybook ansiblePlaybook) {
        ansiblePlaybookMapper.updateByPrimaryKey(ansiblePlaybook);
    }

    @Override
    public void deleteById(int id) {
        ansiblePlaybookMapper.deleteByPrimaryKey(id);
    }

    @Override
    public AnsiblePlaybook getById(int id) {
        return ansiblePlaybookMapper.selectByPrimaryKey(id);
    }

    @Override
    public DataTable<AnsiblePlaybook> queryPageByParam(AnsiblePlaybookParam.AnsiblePlaybookPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(AnsiblePlaybook.class);
        if (StringUtils.isNotBlank(pageQuery.getName())) {
            Example.Criteria criteria = example.createCriteria();
            String likeName = SQLUtil.toLike(pageQuery.getName());
            criteria.andLike("name", likeName);
        }
        example.setOrderByClause("create_time");
        List<AnsiblePlaybook> data = ansiblePlaybookMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

}