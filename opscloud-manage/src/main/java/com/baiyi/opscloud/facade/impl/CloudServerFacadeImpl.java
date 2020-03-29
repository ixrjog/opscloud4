package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.cloud.server.ICloudServer;
import com.baiyi.opscloud.cloud.server.factory.CloudCerverFactory;
import com.baiyi.opscloud.common.base.CloudServerStatus;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.domain.param.cloud.CloudServerParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudServerVO;
import com.baiyi.opscloud.facade.CloudServerFacade;
import com.baiyi.opscloud.service.cloud.OcCloudServerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/17 9:40 上午
 * @Version 1.0
 */
@Service
public class CloudServerFacadeImpl implements CloudServerFacade {

    @Resource
    private OcCloudServerService ocCloudServerService;

    @Override
    public DataTable<OcCloudServerVO.CloudServer> queryCloudServerPage(CloudServerParam.PageQuery pageQuery) {
        DataTable<OcCloudServer> table = ocCloudServerService.queryOcCloudServerByParam(pageQuery);
        List<OcCloudServerVO.CloudServer> page = BeanCopierUtils.copyListProperties(table.getData(), OcCloudServerVO.CloudServer.class);
        DataTable<OcCloudServerVO.CloudServer> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> deleteCloudServerById(int id) {
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerById(id);
        if (ocCloudServer == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_SERVER_NOT_EXIST);
        ocCloudServerService.deleteOcCloudServerById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> syncCloudServerByKey(String key) {
        ICloudServer cloudServer = CloudCerverFactory.getCloudServerByKey(key);
        cloudServer.sync();
        return new BusinessWrapper<>(true);
    }

    @Override
    public void updateCloudServerStatus(int id, int serverId, int cloudServerStatus) {
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerById(id);
        if (cloudServerStatus == CloudServerStatus.REGISTER.getStatus()) {
            ocCloudServer.setServerId(serverId);
        } else {
            ocCloudServer.setServerId(0);
        }
        ocCloudServer.setServerStatus(cloudServerStatus);
        ocCloudServerService.updateOcCloudServer(ocCloudServer);
    }

}
