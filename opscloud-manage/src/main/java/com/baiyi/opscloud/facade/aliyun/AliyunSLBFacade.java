package com.baiyi.opscloud.facade.aliyun;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlb;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;

import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 4:53 下午
 * @Since 1.0
 */
public interface AliyunSLBFacade {

    BusinessWrapper<Boolean> syncSLB();

    DataTable<AliyunSLBVO.LoadBalancer> queryAliyunSLBPage(AliyunSLBParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> refreshSLBListener(String loadBalancerId);

    BusinessWrapper<Boolean> linkNginx(Integer id);

    BusinessWrapper<List<OcAliyunSlb>> queryLinkNginxSLB();

    BusinessWrapper<Map<Integer, List<AliyunSLBVO.BackendServer>>> querySLBListenerBackendServers(String loadBalancerId);


}
