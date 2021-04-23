package com.baiyi.opscloud.cloud.dns.impl;

import com.aliyuncs.domain.model.v20180129.QueryDomainListResponse;
import com.baiyi.opscloud.aliyun.dns.AliyunDomain;
import com.baiyi.opscloud.cloud.dns.AliyunDomainCenter;
import com.baiyi.opscloud.cloud.dns.builder.OcAliyunDomainBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomain;
import com.baiyi.opscloud.service.aliyun.dns.OcAliyunDomainService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:36 下午
 * @Since 1.0
 */

@Component("AliyunDomainCenter")
public class AliyunDomainCenterImpl implements AliyunDomainCenter {

    @Resource
    private AliyunDomain aliyunDomain;

    @Resource
    private OcAliyunDomainService ocAliyunDomainService;

    @Override
    public Boolean syncAliyunDomain() {
        HashMap<String, OcAliyunDomain> map = getDomainMap();
        List<QueryDomainListResponse.Domain> domainList = aliyunDomain.queryDomainList();
        domainList.forEach(domain -> {
            saveDomain(domain);
            map.remove(domain.getInstanceId());
        });
        delDomainByMap(map);
        return true;
    }

    private void saveDomain(QueryDomainListResponse.Domain domain) {
        OcAliyunDomain aliyunDomain = ocAliyunDomainService.queryAliyunDomainByInstanceId(domain.getInstanceId());
        OcAliyunDomain newAliyunDomain = OcAliyunDomainBuilder.build(domain);
        if (aliyunDomain == null) {
            try {
                ocAliyunDomainService.addAliyunDomain(newAliyunDomain);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newAliyunDomain.setId(aliyunDomain.getId());
            newAliyunDomain.setIsActive(aliyunDomain.getIsActive());
            ocAliyunDomainService.updateAliyunDomain(newAliyunDomain);
        }
    }

    private void delDomainByMap(HashMap<String, OcAliyunDomain> map) {
        map.forEach((key, value) -> ocAliyunDomainService.deleteAliyunDomain(value.getId()));
    }

    private HashMap<String, OcAliyunDomain> getDomainMap() {
        List<OcAliyunDomain> domains = ocAliyunDomainService.queryAliyunDomainAll();
        HashMap<String, OcAliyunDomain> map = Maps.newHashMap();
        domains.forEach(domain -> map.put(domain.getInstanceId(), domain));
        return map;
    }
}
