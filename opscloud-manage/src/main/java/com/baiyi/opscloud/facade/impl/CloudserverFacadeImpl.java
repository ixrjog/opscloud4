package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.cloudserver.ICloudserver;
import com.baiyi.opscloud.cloudserver.factory.CloudserverFactory;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.OcCloudserver;
import com.baiyi.opscloud.domain.param.cloudserver.CloudserverParam;
import com.baiyi.opscloud.domain.vo.cloudserver.OcCloudserverVO;
import com.baiyi.opscloud.facade.CloudserverFacade;
import com.baiyi.opscloud.service.cloudserver.OcCloudserverService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/17 9:40 上午
 * @Version 1.0
 */
@Service
public class CloudserverFacadeImpl implements CloudserverFacade {

    @Resource
    private OcCloudserverService ocCloudserverService;

    @Override
    public DataTable<OcCloudserverVO.OcCloudserver> queryCloudserverPage(CloudserverParam.PageQuery pageQuery) {
        DataTable<OcCloudserver> table = ocCloudserverService.queryOcCloudserverByParam(pageQuery);

        List<OcCloudserverVO.OcCloudserver> page = BeanCopierUtils.copyListProperties(table.getData(), OcCloudserverVO.OcCloudserver.class);
        DataTable<OcCloudserverVO.OcCloudserver> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> deleteCloudserverById(int id) {
        OcCloudserver ocCloudserver = ocCloudserverService.queryOcCloudserverById(id);
        if (ocCloudserver == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUDSERVER_NOT_EXIST);
        ocCloudserverService.deleteOcCloudserverById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> syncCloudserverByKey(String key) {
        ICloudserver cloudserver = CloudserverFactory.getCloudserverByKey(key);
        cloudserver.sync();
        return new BusinessWrapper<>(true);
    }
}
