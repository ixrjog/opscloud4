package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.cloud.server.ICloudServer;
import com.baiyi.opscloud.cloud.server.factory.CloudServerFactory;
import com.baiyi.opscloud.common.base.CloudServerKey;
import com.baiyi.opscloud.common.base.CloudServerStatus;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.ByteUtils;
import com.baiyi.opscloud.decorator.cloud.CloudServerDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.domain.param.cloud.CloudServerParam;
import com.baiyi.opscloud.domain.param.cloud.CloudServerStatsParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudServerStatsVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudServerVO;
import com.baiyi.opscloud.facade.CloudServerFacade;
import com.baiyi.opscloud.service.cloud.OcCloudServerDashboardService;
import com.baiyi.opscloud.service.cloud.OcCloudServerService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private OcCloudServerDashboardService ocCloudServerDashboardService;

    @Override
    public DataTable<CloudServerVO.CloudServer> queryCloudServerPage(CloudServerParam.CloudServerPageQuery pageQuery) {
        DataTable<OcCloudServer> table = ocCloudServerService.queryOcCloudServerByParam(pageQuery);
        List<CloudServerVO.CloudServer> page = BeanCopierUtils.copyListProperties(table.getData(), CloudServerVO.CloudServer.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public DataTable<CloudServerVO.CloudServer> queryCloudServerChargePage(CloudServerParam.CloudServerChargePageQuery pageQuery) {
        DataTable<OcCloudServer> table = ocCloudServerService.queryOcCloudServerChargeByParam(pageQuery);
        List<CloudServerVO.CloudServer> page = BeanCopierUtils.copyListProperties(table.getData(), CloudServerVO.CloudServer.class);
        return new DataTable<>(page.stream().map(e -> CloudServerDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
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
        cloudServer.sync(true);
    }

    @Override
    public Boolean syncCloudServerById(int id) {
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerById(id);
        ICloudServer cloudServer = CloudServerFactory.getCloudServerByKey(CloudServerKey.getKey(ocCloudServer.getCloudServerType()));
        return cloudServer.update(ocCloudServer.getRegionId(), ocCloudServer.getInstanceId());
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

    @Override
    public BusinessWrapper<Boolean> cloudServerPowerOn(CloudServerParam.PowerAction param) {
        ICloudServer cloudServer = CloudServerFactory.getCloudServerByKey(param.getKey());
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerByInstanceId(param.getInstanceId());
        if (cloudServer.queryPowerStatus(ocCloudServer.getId()) == 0)
            return cloudServer.start(ocCloudServer.getId());
        return new BusinessWrapper<>(ErrorEnum.CLOUD_SERVER_POWER_STATUS_ERROR);
    }

    @Override
    public BusinessWrapper<Integer> queryPowerStatus(CloudServerParam.PowerAction param) {
        ICloudServer cloudServer = CloudServerFactory.getCloudServerByKey(param.getKey());
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerByInstanceId(param.getInstanceId());
        return new BusinessWrapper<>(cloudServer.queryPowerStatus(ocCloudServer.getId()));
    }


    @Override
    public BusinessWrapper<Boolean> modifyInstanceChargeType(CloudServerParam.ModifyInstanceChargeType param) {
        ICloudServer cloudServer = CloudServerFactory.getCloudServerByKey(param.getKey());
        cloudServer.modifyInstanceChargeType(param.getInstanceId(), param.getChargeType());
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<List<CloudServerStatsVO.ServerMonthStats>> queryServerMonthStatsReport(Integer queryYear) {
        List<CloudServerStatsVO.ServerMonthStats> serverMonthStats = ocCloudServerDashboardService.queryServerMonthStatsReport(queryYear);
        return new BusinessWrapper<>(serverMonthStats);
    }

    @Override
    public BusinessWrapper<CloudServerStatsVO.ServerResStats> queryServerResStatsReport(Integer cloudServerType) {
        CloudServerStatsVO.ServerResStats serverResStats = ocCloudServerDashboardService.queryServerResStatsReport(cloudServerType);
        ByteUtils.ByteResult memory = ByteUtils.byteFormat(serverResStats.getMemoryTotal(), ByteUtils.Unit.MB);
        ByteUtils.ByteResult disk = ByteUtils.byteFormat(serverResStats.getDiskTotal(), ByteUtils.Unit.GB);
        serverResStats.setDisk(disk.getSize());
        serverResStats.setDiskUnit(disk.getUnit());
        serverResStats.setMemory(memory.getSize());
        serverResStats.setMemoryUnit(memory.getUnit());
        return new BusinessWrapper<>(serverResStats);
    }

    @Override
    public BusinessWrapper<List<CloudServerStatsVO.ServerMonthStatsByType>> queryServerMonthStatsReportByType(CloudServerStatsParam.MonthStats param) {
        List<CloudServerStatsVO.ServerMonthStatsByType> serverMonthStatsByType = ocCloudServerDashboardService.queryServerMonthStatsReportByType(param);
        return new BusinessWrapper<>(serverMonthStatsByType);
    }
}
