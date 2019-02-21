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
     *
     * @param dnsItem
     * @return
     */
    long getDnsmasqSize(
            @Param("dnsItem") String dnsItem
    );

    /**
     * 查询dnsmasq详情页
     *
     * @param dnsItem
     * @param pageStart
     * @param length
     * @return
     */
    List<DnsmasqDO> getDnsmasqPage(
            @Param("dnsItem") String dnsItem,
            @Param("pageStart") long pageStart, @Param("length") int length);

    /**
     * 最多查询500条解析记录
     * @return
     */
    List<DnsmasqDO> queryAllDnsmasq();

    /**
     * 新增指定dnsmasq配置
     *
     * @param dnsmasqDO
     * @return
     */
    int addDnsmasq(DnsmasqDO dnsmasqDO);

    /**
     * 更新指定dnsmasq配置
     *
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
