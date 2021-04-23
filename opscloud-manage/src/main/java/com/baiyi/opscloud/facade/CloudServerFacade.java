package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.CloudServerParam;
import com.baiyi.opscloud.domain.param.cloud.CloudServerStatsParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudServerStatsVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudServerVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/17 9:39 上午
 * @Version 1.0
 */
public interface CloudServerFacade {

    DataTable<CloudServerVO.CloudServer> queryCloudServerPage(CloudServerParam.CloudServerPageQuery pageQuery);

    DataTable<CloudServerVO.CloudServer> queryCloudServerChargePage(CloudServerParam.CloudServerChargePageQuery pageQuery);

    BusinessWrapper<Boolean> deleteCloudServerById(int id);

    void syncCloudServerByKey(String key);

    Boolean syncCloudServerById(int id);

    void updateCloudServerStatus(int id, int serverId, int cloudServerStatus);

    BusinessWrapper<Boolean> deleteCloudServer(CloudServerParam.DeleteInstance param);

    BusinessWrapper<Boolean> cloudServerPowerOn(CloudServerParam.PowerAction param);

    BusinessWrapper<Integer> queryPowerStatus(CloudServerParam.PowerAction param);

    BusinessWrapper<Boolean> modifyInstanceChargeType(CloudServerParam.ModifyInstanceChargeType param);

    BusinessWrapper<List<CloudServerStatsVO.ServerMonthStats>> queryServerMonthStatsReport(Integer queryYear);

    BusinessWrapper<CloudServerStatsVO.ServerResStats> queryServerResStatsReport(Integer cloudServerType);

    BusinessWrapper<List<CloudServerStatsVO.ServerMonthStatsByType>> queryServerMonthStatsReportByType(CloudServerStatsParam.MonthStats param);
}
