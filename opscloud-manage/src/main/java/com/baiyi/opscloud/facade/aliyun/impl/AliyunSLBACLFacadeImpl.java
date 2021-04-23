package com.baiyi.opscloud.facade.aliyun.impl;

import com.baiyi.opscloud.cloud.slb.AliyunSLBACLCenter;
import com.baiyi.opscloud.convert.aliyun.slb.AliyunSLBAccessControlEntryConvert;
import com.baiyi.opscloud.decorator.aliyun.slb.AliyunSLBAccessControlDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAcl;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAclEntry;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;
import com.baiyi.opscloud.facade.aliyun.AliyunSLBACLFacade;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSLBACLService;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbAclEntryService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 5:29 下午
 * @Since 1.0
 */

@Component
public class AliyunSLBACLFacadeImpl implements AliyunSLBACLFacade {

    @Resource
    private AliyunSLBACLCenter aliyunSLBACLCenter;

    @Resource
    private OcAliyunSLBACLService ocAliyunSLBACLService;

    @Resource
    private OcAliyunSlbAclEntryService ocAliyunSlbAclEntryService;

    @Resource
    private AliyunSLBAccessControlDecorator aliyunSLBAccessControlDecorator;

    @Override
    public BusinessWrapper<Boolean> syncSLBACL() {
        return aliyunSLBACLCenter.syncSLBACL();
    }

    @Override
    public DataTable<AliyunSLBVO.AccessControl> queryAccessControlPage(AliyunSLBParam.ACLPageQuery pageQuery) {
        DataTable<OcAliyunSlbAcl> table = ocAliyunSLBACLService.queryOcAliyunSlbAclPage(pageQuery);
        List<AliyunSLBVO.AccessControl> loadBalancerList = aliyunSLBAccessControlDecorator.decoratorVOList(table.getData());
        return new DataTable<>(loadBalancerList, table.getTotalNum());
    }

    @Override
    public List<AliyunSLBVO.AccessControlEntry> queryAccessControlEntry(String slbAclId) {
        List<OcAliyunSlbAclEntry> entryList = ocAliyunSlbAclEntryService.queryOcAliyunSlbAclEntryBySlbAclId(slbAclId);
        return AliyunSLBAccessControlEntryConvert.toVOList(entryList);
    }
}
