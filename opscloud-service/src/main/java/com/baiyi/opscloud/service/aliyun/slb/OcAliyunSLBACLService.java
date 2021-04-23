package com.baiyi.opscloud.service.aliyun.slb;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAcl;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 2:37 下午
 * @Since 1.0
 */
public interface OcAliyunSLBACLService {

    OcAliyunSlbAcl queryOcAliyunSlbAcl(Integer id);

    void addOcAliyunSlbAcl(OcAliyunSlbAcl ocAliyunSlbAcl);

    void updateOcAliyunSlbAcl(OcAliyunSlbAcl ocAliyunSlbAcl);

    void deleteOcAliyunSlbAcl(int id);

    OcAliyunSlbAcl queryOcAliyunSlbAclBySlbAclId(String slbAclId);

    List<OcAliyunSlbAcl> queryOcAliyunSlbAclAll();

    DataTable<OcAliyunSlbAcl> queryOcAliyunSlbAclPage(AliyunSLBParam.ACLPageQuery pageQuery);
}
