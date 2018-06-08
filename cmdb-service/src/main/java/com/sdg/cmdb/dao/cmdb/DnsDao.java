package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.dns.DnsmasqDO;
import com.sdg.cmdb.domain.server.ServerDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liangjian on 2017/7/11.
 */
@Component
public interface DnsDao {

    /**
     * 获取dnsmasq配置条目
     * @param dnsGroupId
     * @param itemType
     * @param queryItemValue
     * @return
     */
    long getDnsmasqSize(
            @Param("dnsGroupId") long dnsGroupId,
            @Param("itemType") int itemType,
            @Param("queryItemValue") String queryItemValue
    );

    /**
     * 查询dnsmasq详情页
     * @param dnsGroupId
     * @param itemType
     * @param queryItemValue
     * @param pageStart
     * @param length
     * @return
     */
    List<DnsmasqDO> getDnsmasqPage(
            @Param("dnsGroupId") long dnsGroupId,
            @Param("itemType") int itemType,
            @Param("queryItemValue") String queryItemValue,
            @Param("pageStart") long pageStart, @Param("length") int length);


    /**
     * 按dnsGroupId和itemType查询
     *
     * @param dnsGroupId
     * @param itemType
     * @return
     */
    List<DnsmasqDO> queryDnsmasqByGroupIdAndItemType(
            @Param("dnsGroupId") long dnsGroupId,
            @Param("itemType") int itemType);


    /**
     * 按dnsGroupId和item和itemValue查询
     * @param dnsGroupId
     * @param item
     * @param itemValue
     * @return
     */
    DnsmasqDO queryDnsmasqByGroupIdAndItemAndItemValue(
            @Param("dnsGroupId") long dnsGroupId,
            @Param("item") String item,
            @Param("itemValue") String itemValue
    );

    /**
     * 新增指定dnsmasq配置
     * @param dnsmasqDO
     * @return
     */
    int addDnsmasq(DnsmasqDO dnsmasqDO);

    /**
     * 更新指定dnsmasq配置
     * @param dnsmasqDO
     * @return
     */
    int updateDnsmasq(DnsmasqDO dnsmasqDO);


    /**
     * 删除指定dnsmasq信息
     *
     * @param id
     * @return
     */
    int delDnsmasq(@Param("id") long id);

}
