package com.baiyi.opscloud.service.ticket.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import com.baiyi.opscloud.mapper.opscloud.OcWorkorderTicketEntryMapper;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketEntryService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/27 4:54 下午
 * @Version 1.0
 */
@Service
public class OcWorkorderTicketEntryServiceImpl implements OcWorkorderTicketEntryService {

    @Resource
    private OcWorkorderTicketEntryMapper ocWorkorderTicketEntryMapper;

    @Override
    public List<OcWorkorderTicketEntry> queryOcWorkorderTicketEntryByTicketId(int workorderTicketId) {
        Example example = new Example(OcWorkorderTicketEntry.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workorderTicketId", workorderTicketId);
        return ocWorkorderTicketEntryMapper.selectByExample(example);
    }

    @Override
    public int countOcWorkorderTicketEntryByTicketId(int workorderTicketId) {
        Example example = new Example(OcWorkorderTicketEntry.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workorderTicketId", workorderTicketId);
        return ocWorkorderTicketEntryMapper.selectCountByExample(example);
    }

    @Override
    public OcWorkorderTicketEntry queryOcWorkorderTicketEntryById(int id) {
        return ocWorkorderTicketEntryMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateOcWorkorderTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry) {
        ocWorkorderTicketEntryMapper.updateByPrimaryKey(ocWorkorderTicketEntry);
    }

    @Override
    public void addOcWorkorderTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry) {
        ocWorkorderTicketEntryMapper.insert(ocWorkorderTicketEntry);
    }

    @Override
    public void deleteOcWorkorderTicketEntryById(int id) {
        ocWorkorderTicketEntryMapper.deleteByPrimaryKey(id);
    }

}
