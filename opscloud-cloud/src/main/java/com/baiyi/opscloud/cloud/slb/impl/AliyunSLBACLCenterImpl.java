package com.baiyi.opscloud.cloud.slb.impl;

import com.aliyuncs.slb.model.v20140515.DescribeAccessControlListAttributeResponse;
import com.aliyuncs.slb.model.v20140515.DescribeAccessControlListsResponse;
import com.baiyi.opscloud.aliyun.slb.AliyunSLBACL;
import com.baiyi.opscloud.cloud.slb.AliyunSLBACLCenter;
import com.baiyi.opscloud.cloud.slb.builder.OcAliyunSLBCACLBuilder;
import com.baiyi.opscloud.cloud.slb.builder.OcAliyunSlbAclEntryBuilder;
import com.baiyi.opscloud.cloud.slb.builder.OcAliyunSlbAclListenerBuilder;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlb;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAcl;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAclEntry;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAclListener;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSLBACLService;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSLBService;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbAclEntryService;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbAclListenerService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:40 下午
 * @Since 1.0
 */

@Component("AliyunSLBACLCenter")
public class AliyunSLBACLCenterImpl implements AliyunSLBACLCenter {


    @Resource
    private OcAliyunSLBACLService ocAliyunSLBACLService;

    @Resource
    private OcAliyunSLBService ocAliyunSLBService;

    @Resource
    private AliyunSLBACL aliyunSLBACL;

    @Resource
    private OcAliyunSlbAclEntryService ocAliyunSlbAclEntryService;

    @Resource
    private OcAliyunSlbAclListenerService ocAliyunSlbAclListenerService;

    @Override
    public BusinessWrapper<Boolean> syncSLBACL() {
        HashMap<String, OcAliyunSlbAcl> map = getSLBAclMap();
        List<DescribeAccessControlListsResponse.Acl> aclList = aliyunSLBACL.queryLoadBalancerACLList();
        aclList.forEach(acl -> {
            saveSLBAcl(acl);
            map.remove(acl.getAclId());
            refreshSLBAcl(acl);
        });
        delSLBAclByMap(map);
        return BusinessWrapper.SUCCESS;
    }

    private void saveSLBAcl(DescribeAccessControlListsResponse.Acl acl) {
        OcAliyunSlbAcl ocAliyunSlbAcl = ocAliyunSLBACLService.queryOcAliyunSlbAclBySlbAclId(acl.getAclId());
        OcAliyunSlbAcl newOcAliyunSlbAcl = OcAliyunSLBCACLBuilder.build(acl);
        if (ocAliyunSlbAcl == null) {
            try {
                ocAliyunSLBACLService.addOcAliyunSlbAcl(newOcAliyunSlbAcl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newOcAliyunSlbAcl.setId(ocAliyunSlbAcl.getId());
            ocAliyunSLBACLService.updateOcAliyunSlbAcl(newOcAliyunSlbAcl);
        }
    }

    private void delSLBAclByMap(HashMap<String, OcAliyunSlbAcl> map) {
        map.forEach((key, value) -> {
            ocAliyunSLBACLService.deleteOcAliyunSlbAcl(value.getId());
            ocAliyunSlbAclEntryService.deleteOcAliyunSlbAclEntryBySlbAclId(value.getSlbAclId());
            ocAliyunSlbAclListenerService.deleteOcAliyunSlbAclListenerBySlbAclId(value.getSlbAclId());
        });
    }

    private HashMap<String, OcAliyunSlbAcl> getSLBAclMap() {
        List<OcAliyunSlbAcl> aclList = ocAliyunSLBACLService.queryOcAliyunSlbAclAll();
        HashMap<String, OcAliyunSlbAcl> map = Maps.newHashMap();
        aclList.forEach(acl -> map.put(acl.getSlbAclId(), acl));
        return map;
    }

    private void refreshSLBAcl(DescribeAccessControlListsResponse.Acl acl) {
        ocAliyunSlbAclEntryService.deleteOcAliyunSlbAclEntryBySlbAclId(acl.getAclId());
        ocAliyunSlbAclListenerService.deleteOcAliyunSlbAclListenerBySlbAclId(acl.getAclId());
        DescribeAccessControlListAttributeResponse aclDetail = aliyunSLBACL.queryAccessControlDetail(acl.getAclId());
        if (aclDetail == null)
            return;
        List<DescribeAccessControlListAttributeResponse.AclEntry> aclEntryList = aclDetail.getAclEntrys();
        List<DescribeAccessControlListAttributeResponse.RelatedListener> relatedListenerList = aclDetail.getRelatedListeners();
        if (!CollectionUtils.isEmpty(aclEntryList)) {
            List<OcAliyunSlbAclEntry> vcAliyunSlbAclEntryList = OcAliyunSlbAclEntryBuilder.buildList(acl.getAclId(), aclEntryList);
            ocAliyunSlbAclEntryService.addOcAliyunSlbAclEntryList(vcAliyunSlbAclEntryList);
        }

        if (!CollectionUtils.isEmpty(relatedListenerList)) {
            List<OcAliyunSlbAclListener> vcAliyunSlbListenerList = OcAliyunSlbAclListenerBuilder.buildList(acl, relatedListenerList);
            vcAliyunSlbListenerList.forEach(vcAliyunSlbAclListener -> {
                OcAliyunSlb ocAliyunSlb = ocAliyunSLBService.queryOcAliyunSlbByLoadBalancerId(vcAliyunSlbAclListener.getLoadBalancerId());
                vcAliyunSlbAclListener.setLoadBalancerName(ocAliyunSlb != null ? ocAliyunSlb.getLoadBalancerName() : "unknown");
            });
            ocAliyunSlbAclListenerService.addOcAliyunSlbAclListenerList(vcAliyunSlbListenerList);
        }
    }
}
