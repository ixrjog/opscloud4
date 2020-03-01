package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.cloud.server.ICloudServer;
import com.baiyi.opscloud.cloud.server.factory.CloudCerverFactory;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.OcCloudServer;
import com.baiyi.opscloud.domain.param.cloudserver.CloudServerParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudServerVO;
import com.baiyi.opscloud.facade.CloudServerFacade;
import com.baiyi.opscloud.service.cloudserver.OcCloudServerService;
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
    public DataTable<OcCloudServerVO.OcCloudServer> queryCloudServerPage(CloudServerParam.PageQuery pageQuery) {
        DataTable<OcCloudServer> table = ocCloudServerService.queryOcCloudServerByParam(pageQuery);

        List<OcCloudServerVO.OcCloudServer> page = BeanCopierUtils.copyListProperties(table.getData(), OcCloudServerVO.OcCloudServer.class);
        DataTable<OcCloudServerVO.OcCloudServer> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> deleteCloudServerById(int id) {
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerById(id);
        if (ocCloudServer == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUDSERVER_NOT_EXIST);
        ocCloudServerService.deleteOcCloudServerById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> syncCloudServerByKey(String key) {
        ICloudServer cloudServer = CloudCerverFactory.getCloudServerByKey(key);
        cloudServer.sync();
        return new BusinessWrapper<>(true);
    }
}
