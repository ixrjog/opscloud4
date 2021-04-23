package com.baiyi.opscloud.facade.aliyun.impl;

import com.aliyuncs.slb.model.v20140515.DescribeHealthStatusResponse;
import com.baiyi.opscloud.cloud.slb.AliyunSLBCenter;
import com.baiyi.opscloud.decorator.aliyun.slb.AliyunSLBBackendServerDecorator;
import com.baiyi.opscloud.decorator.aliyun.slb.AliyunSLBDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlb;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;
import com.baiyi.opscloud.facade.aliyun.AliyunSLBFacade;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSLBService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 4:53 下午
 * @Since 1.0
 */

@Component("AliyunSLBFacade")
public class AliyunSLBFacadeImpl implements AliyunSLBFacade {

    @Resource
    private AliyunSLBCenter aliyunSLBCenter;

    @Resource
    private OcAliyunSLBService ocAliyunSLBService;

    @Resource
    private AliyunSLBDecorator aliyunSLBDecorator;

    @Resource
    private AliyunSLBBackendServerDecorator aliyunSLBBackendServerDecorator;


    @Override
    public BusinessWrapper<Boolean> syncSLB() {
        return aliyunSLBCenter.syncSLB();
    }

    @Override
    public DataTable<AliyunSLBVO.LoadBalancer> queryAliyunSLBPage(AliyunSLBParam.PageQuery pageQuery) {
        DataTable<OcAliyunSlb> table = ocAliyunSLBService.queryOcAliyunSlbPage(pageQuery);
        List<AliyunSLBVO.LoadBalancer> loadBalancerList = aliyunSLBDecorator.decoratorVOList(table.getData());
        return new DataTable<>(loadBalancerList, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> refreshSLBListener(String loadBalancerId) {
        return aliyunSLBCenter.refreshSLBListener(loadBalancerId);
    }

    @Override
    public BusinessWrapper<Boolean> linkNginx(Integer id) {
        OcAliyunSlb ocAliyunSlb = ocAliyunSLBService.queryOcAliyunSlbById(id);
        ocAliyunSlb.setIsLinkNginx(ocAliyunSlb.getIsLinkNginx().equals(0) ? 1 : 0);
        ocAliyunSLBService.updateOcAliyunSlb(ocAliyunSlb);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<List<OcAliyunSlb>> queryLinkNginxSLB() {
        List<OcAliyunSlb> vcAliyunSlbList = ocAliyunSLBService.queryLinkNginxSLB();
        return new BusinessWrapper<>(vcAliyunSlbList);
    }

    @Override
    public BusinessWrapper<Map<Integer, List<AliyunSLBVO.BackendServer>>> querySLBListenerBackendServers(String loadBalancerId) {
        List<DescribeHealthStatusResponse.BackendServer> backendServerList = aliyunSLBCenter.querySLBListenerBackendServers(loadBalancerId);
        List<AliyunSLBVO.BackendServer> backendServerVOList = aliyunSLBBackendServerDecorator.decoratorVOList(backendServerList);
        Map<Integer, List<AliyunSLBVO.BackendServer>> map = backendServerVOList.stream().collect(Collectors.groupingBy(AliyunSLBVO.BackendServer::getListenerPort));
        return new BusinessWrapper<>(map);
    }
}
