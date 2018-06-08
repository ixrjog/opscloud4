package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.dns.DnsmasqDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liangjian on 2017/7/11.
 */
public interface DnsService {


    TableVO<List<DnsmasqDO>> getDnsmasqPage(long dnsGroupId, int itemType, String queryItemValue, long pageStart, int length);

    BusinessWrapper<Boolean> saveDnsmasq(DnsmasqDO dnsmasqDO);

    BusinessWrapper<Boolean> delDnsmasq(long id);

}
