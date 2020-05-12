package com.baiyi.opscloud.service.ansible.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnsiblePlaybook;
import com.baiyi.opscloud.domain.param.ansible.AnsiblePlaybookParam;
import com.baiyi.opscloud.mapper.opscloud.OcAnsiblePlaybookMapper;
import com.baiyi.opscloud.service.ansible.OcAnsiblePlaybookService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/13 4:39 下午
 * @Version 1.0
 */
@Service
public class OcAnsiblePlaybookServiceImpl implements OcAnsiblePlaybookService {

    @Resource
    private OcAnsiblePlaybookMapper ocAnsiblePlaybookMapper;

    @Override
    public DataTable<OcAnsiblePlaybook> queryOcAnsiblePlaybookByParam(AnsiblePlaybookParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcAnsiblePlaybook> ocAnsiblePlaybookList = ocAnsiblePlaybookMapper.queryOcAnsiblePlaybookByParam(pageQuery);
        return new DataTable<>(ocAnsiblePlaybookList, page.getTotal());
    }

    @Override
    public OcAnsiblePlaybook queryOcAnsiblePlaybookById(int id) {
        return ocAnsiblePlaybookMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcAnsiblePlaybook(OcAnsiblePlaybook ocAnsiblePlaybook) {
        ocAnsiblePlaybookMapper.insert(ocAnsiblePlaybook);
    }

    @Override
    public void updateOcAnsiblePlaybook(OcAnsiblePlaybook ocAnsiblePlaybook) {
        ocAnsiblePlaybookMapper.updateByPrimaryKey(ocAnsiblePlaybook);
    }

    @Override
    public void deleteOcAnsiblePlaybookById(int id) {
        ocAnsiblePlaybookMapper.deleteByPrimaryKey(id);
    }
}
