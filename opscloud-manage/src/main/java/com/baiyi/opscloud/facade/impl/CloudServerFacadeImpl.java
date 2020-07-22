package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.cloud.server.ICloudServer;
import com.baiyi.opscloud.cloud.server.factory.CloudServerFactory;
import com.baiyi.opscloud.common.base.CloudServerStatus;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.domain.param.cloud.CloudServerParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudServerVO;
import com.baiyi.opscloud.facade.CloudServerFacade;
import com.baiyi.opscloud.service.cloud.OcCloudServerService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.base.Global.ASYNC_POOL_TASK_EXECUTOR;

/**
 * @Author baiyi
 * @Date 2020/2/17 9:40 上午
 * @Version 1.0
 */
@Service("CloudServerFacade")
public class CloudServerFacadeImpl implements CloudServerFacade {

    @Resource
    private OcCloudServerService ocCloudServerService;

    @Override
    public DataTable<CloudServerVO.CloudServer> queryCloudServerPage(CloudServerParam.PageQuery pageQuery) {
        DataTable<OcCloudServer> table = ocCloudServerService.queryOcCloudServerByParam(pageQuery);
        List<CloudServerVO.CloudServer> page = BeanCopierUtils.copyListProperties(table.getData(), CloudServerVO.CloudServer.class);
        return new DataTable<>(page, table.getTotalNum());
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
    @Async(value = ASYNC_POOL_TASK_EXECUTOR)
    public void syncCloudServerByKey(String key) {
        ICloudServer cloudServer = CloudServerFactory.getCloudServerByKey(key);
        cloudServer.sync();
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

    @Override
    public BusinessWrapper<Boolean> deleteCloudServer(CloudServerParam.DeleteInstance param) {
        ICloudServer cloudServer = CloudServerFactory.getCloudServerByKey(param.getKey());
        if (!cloudServer.delete(param.getInstanceId())) {
            return new BusinessWrapper<>(ErrorEnum.CLOUD_SERVER_DELETE_FAIL);
        }
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerByInstanceId(param.getInstanceId());
        ocCloudServerService.deleteOcCloudServerById(ocCloudServer.getId());
        return BusinessWrapper.SUCCESS;
    }
}
