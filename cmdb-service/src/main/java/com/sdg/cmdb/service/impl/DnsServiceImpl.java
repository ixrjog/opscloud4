package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.DnsDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.dns.DnsmasqDO;
import com.sdg.cmdb.service.DnsService;
import com.sdg.cmdb.service.configurationProcessor.DnsmasqFileProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liangjian on 2017/7/11.
 */
@Service
public class DnsServiceImpl implements DnsService {

    @Resource
    private DnsDao dnsDao;

    @Autowired
    private DnsmasqFileProcessorService dnsService;

    @Override
    public TableVO<List<DnsmasqDO>> getDnsmasqPage(String dnsItem, long pageStart, int length) {
        long size = dnsDao.getDnsmasqSize(dnsItem);
        List<DnsmasqDO> list = dnsDao.getDnsmasqPage(dnsItem, pageStart * length, length);
        return new TableVO<>(size, list);
    }

    @Override
    public String getDnsmasqConf() {
        return dnsService.getFile();
    }

    @Override
    public BusinessWrapper<Boolean> buildDnsmasqConf(){
        return new BusinessWrapper<Boolean>(dnsService.buildFile());
    }

    @Override
    public BusinessWrapper<Boolean> saveDnsmasq(DnsmasqDO dnsmasqDO) {
        try {
            if (dnsmasqDO.getId() != 0) {
                updateDnsmasq(dnsmasqDO);
            } else {
                addDnsmasq(dnsmasqDO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
        return new BusinessWrapper<>(true);
    }

    private boolean addDnsmasq(DnsmasqDO dnsmasqDO) {
        try {
            dnsDao.addDnsmasq(dnsmasqDO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateDnsmasq(DnsmasqDO dnsmasqDO) {
        try {
            dnsDao.updateDnsmasq(dnsmasqDO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public BusinessWrapper<Boolean> delDnsmasq(long id) {
        try {
            dnsDao.delDnsmasq(id);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }


}
