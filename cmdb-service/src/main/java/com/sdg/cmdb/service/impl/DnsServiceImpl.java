package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.DnsDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.dns.DnsmasqDO;
import com.sdg.cmdb.service.DnsService;
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

    @Override
    public TableVO<List<DnsmasqDO>> getDnsmasqPage(long dnsGroupId, int itemType, String queryItemValue, long pageStart, int length) {
        long size = dnsDao.getDnsmasqSize(dnsGroupId, itemType, queryItemValue);
        List<DnsmasqDO> list = dnsDao.getDnsmasqPage(dnsGroupId, itemType, queryItemValue, pageStart * length, length);
        return new TableVO<>(size, list);
    }

    @Override
    public BusinessWrapper<Boolean> saveDnsmasq(DnsmasqDO dnsmasqDO) {
        try {
            DnsmasqDO dnsmasq = dnsDao.queryDnsmasqByGroupIdAndItemAndItemValue(dnsmasqDO.getDnsGroupId(), dnsmasqDO.getItem(), dnsmasqDO.getItemValue());
            if (dnsmasq != null && dnsmasqDO.getId() != 0) {
                if (updateDnsmasq(dnsmasqDO)) {
                    return new BusinessWrapper<>(true);
                } else {
                    return new BusinessWrapper<>(ErrorCode.serverFailure);
                }
            } else {
                if (addDnsmasq(dnsmasqDO)) {
                    return new BusinessWrapper<>(true);
                } else {
                    return new BusinessWrapper<>(ErrorCode.serverFailure);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
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
