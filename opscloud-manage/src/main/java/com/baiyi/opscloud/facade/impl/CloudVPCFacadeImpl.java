package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.cloud.image.ICloudImage;
import com.baiyi.opscloud.cloud.image.factory.CloudImageFactory;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.CloudVPCDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.OcCloudVpc;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudVPCVO;
import com.baiyi.opscloud.facade.CloudVPCFacade;
import com.baiyi.opscloud.service.cloud.OcCloudVpcService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/3/18 7:02 下午
 * @Version 1.0
 */
@Service
public class CloudVPCFacadeImpl implements CloudVPCFacade {

    @Resource
    private OcCloudVpcService ocCloudVpcService;

    @Resource
    private CloudVPCDecorator cloudVPCDecorator;

    @Override
    public DataTable<OcCloudVPCVO.CloudVpc> fuzzyQueryCloudVPCPage(CloudVPCParam.PageQuery pageQuery) {
        DataTable<OcCloudVpc> table = ocCloudVpcService.fuzzyQueryOcCloudVpcByParam(pageQuery);
        List<OcCloudVPCVO.CloudVpc> page = BeanCopierUtils.copyListProperties(table.getData(), OcCloudVPCVO.CloudVpc.class);
        DataTable<OcCloudVPCVO.CloudVpc> dataTable
                = new DataTable<>(page.stream().map(e -> cloudVPCDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> syncCloudVPCByKey(String key) {
        ICloudImage cloudImage = CloudImageFactory.getCloudImageByKey(key);
        return new BusinessWrapper<>(cloudImage.sync());
    }

    @Override
    public BusinessWrapper<Boolean> deleteCloudVPCById(int id) {
        OcCloudVpc ocCloudVpc = ocCloudVpcService.queryOcCloudVpcById(id);
        if (ocCloudVpc == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_VPC_NOT_EXIST);
        ocCloudVpcService.deleteOcCloudVpcById(id);
        return BusinessWrapper.SUCCESS;
    }
}
