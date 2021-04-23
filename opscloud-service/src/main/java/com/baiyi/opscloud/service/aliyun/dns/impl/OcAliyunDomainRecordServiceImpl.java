package com.baiyi.opscloud.service.aliyun.dns.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomainRecord;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainRecordParam;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunDomainRecordMapper;
import com.baiyi.opscloud.service.aliyun.dns.OcAliyunDomainRecordService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 2:44 下午
 * @Since 1.0
 */

@Service
public class OcAliyunDomainRecordServiceImpl implements OcAliyunDomainRecordService {

    @Resource
    private OcAliyunDomainRecordMapper ocAliyunDomainRecordMapper;

    @Override
    public OcAliyunDomainRecord queryOcAliyunDomainRecordById(Integer id) {
        return ocAliyunDomainRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcAliyunDomainRecord(OcAliyunDomainRecord ocAliyunDomainRecord) {
        ocAliyunDomainRecordMapper.insert(ocAliyunDomainRecord);
    }

    @Override
    public void updateOcAliyunDomainRecord(OcAliyunDomainRecord ocAliyunDomainRecord) {
        ocAliyunDomainRecordMapper.updateByPrimaryKey(ocAliyunDomainRecord);
    }

    @Override
    public void deleteOcAliyunDomainRecord(int id) {
        ocAliyunDomainRecordMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteOcAliyunDomainRecordByRecordId(String recordId) {
        Example example = new Example(OcAliyunDomainRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("recordId", recordId);
        ocAliyunDomainRecordMapper.deleteByExample(example);
    }

    @Override
    public List<OcAliyunDomainRecord> queryAliyunDomainRecordByDomainName(String domainName) {
        Example example = new Example(OcAliyunDomainRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("domainName", domainName);
        return ocAliyunDomainRecordMapper.selectByExample(example);
    }

    @Override
    public List<OcAliyunDomainRecord> queryAliyunDomainRecordByNameAndRr(String domainName, String recordRr) {
        Example example = new Example(OcAliyunDomainRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("domainName", domainName);
        criteria.andEqualTo("recordRr", recordRr);
        return ocAliyunDomainRecordMapper.selectByExample(example);
    }

    @Override
    public OcAliyunDomainRecord queryAliyunDomainRecordByRecordId(String recordId) {
        Example example = new Example(OcAliyunDomainRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("recordId", recordId);
        return ocAliyunDomainRecordMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<OcAliyunDomainRecord> queryOcAliyunDomainRecordPage(AliyunDomainRecordParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAliyunDomainRecord> ocAliyunDomainRecordList = ocAliyunDomainRecordMapper.queryOcAliyunDomainRecordPage(pageQuery);
        return new DataTable<>(ocAliyunDomainRecordList, page.getTotal());
    }
}
