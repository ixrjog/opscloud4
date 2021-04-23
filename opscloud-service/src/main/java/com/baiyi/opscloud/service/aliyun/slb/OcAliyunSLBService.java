package com.baiyi.opscloud.service.aliyun.slb;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlb;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 2:38 下午
 * @Since 1.0
 */
public interface OcAliyunSLBService {

    OcAliyunSlb queryOcAliyunSlbById(Integer id);

    void addOcAliyunSlb(OcAliyunSlb ocAliyunSlb);

    void updateOcAliyunSlb(OcAliyunSlb ocAliyunSlb);

    void deleteOcAliyunSlb(int id);

    OcAliyunSlb queryOcAliyunSlbByLoadBalancerId(String loadBalancerId);

    List<OcAliyunSlb> queryOcAliyunSlbByLoadBalancerIdList(List<String> loadBalancerIdList);

    List<OcAliyunSlb> queryOcAliyunSlbAll();

    DataTable<OcAliyunSlb> queryOcAliyunSlbPage(AliyunSLBParam.PageQuery pageQuery);

    List<OcAliyunSlb> queryLinkNginxSLB();
}
