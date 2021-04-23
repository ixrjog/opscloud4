package com.baiyi.opscloud.facade.opscloud.impl;

import com.baiyi.opscloud.builder.OpscloudInstanceBuilder;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.HostUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcInstance;
import com.baiyi.opscloud.domain.param.opscloud.OpscloudInstanceParam;
import com.baiyi.opscloud.domain.vo.opscloud.HealthVO;
import com.baiyi.opscloud.domain.vo.opscloud.OpscloudVO;
import com.baiyi.opscloud.facade.opscloud.OpscloudInstanceFacade;
import com.baiyi.opscloud.service.opscloud.OcInstanceService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/24 4:33 下午
 * @Since 1.0
 */

@Component("OpscloudInstanceFacade")
public class OpscloudInstanceFacadeImpl implements OpscloudInstanceFacade, InitializingBean {

    @Resource
    private OcInstanceService ocInstanceService;

    private static final String HEALTH_OK = "OK";

    @Override
    public HealthVO.Health checkHealth() {
        try {
            InetAddress inetAddress = HostUtils.getInetAddress();
            OcInstance ocInstance = ocInstanceService.queryOcInstanceByHostIp(inetAddress.getHostAddress());
            if (ocInstance == null)
                return getHealth("ERROR", false);
            if (ocInstance.getIsActive()) {
                return getHealth(HEALTH_OK, true);
            } else {
                return getHealth("INACTIVE", false);
            }
        } catch (UnknownHostException ignored) {
        }
        return getHealth("ERROR", false);
    }

    private HealthVO.Health getHealth(String status, boolean isHealth) {
        HealthVO.Health health = new HealthVO.Health();
        health.setStatus(status);
        health.setHealth(isHealth);
        return health;
    }

    @Override
    public DataTable<OpscloudVO.Instance> queryOpscloudInstancePage(OpscloudInstanceParam.PageQuery pageQuery) {
        DataTable<OcInstance> table = ocInstanceService.queryOcInstanceByParam(pageQuery);
        List<OpscloudVO.Instance> page = BeanCopierUtils.copyListProperties(table.getData(), OpscloudVO.Instance.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> setOpscloudInstanceActive(int id) {
        OcInstance ocInstance = ocInstanceService.queryOcInstanceById(id);
        ocInstance.setIsActive(!ocInstance.getIsActive());
        ocInstanceService.updateOcInstance(ocInstance);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public void afterPropertiesSet() {
        this.register();
    }

    private void register() {
        try {
            InetAddress inetAddress = HostUtils.getInetAddress();
            if (ocInstanceService.queryOcInstanceByHostIp(inetAddress.getHostAddress()) != null) return;
            OcInstance ocInstance = OpscloudInstanceBuilder.build(inetAddress);
            ocInstanceService.addOcInstance(ocInstance);
        } catch (UnknownHostException ignored) {
        }
    }

    @Override
    public BusinessWrapper<Boolean> delOpscloudInstanceById(int id) {
        ocInstanceService.delOcInstance(id);
        return BusinessWrapper.SUCCESS;
    }
}
