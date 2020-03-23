package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.cloud.image.ICloudImage;
import com.baiyi.opscloud.cloud.image.factory.CloudImageFactory;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.OcCloudImage;
import com.baiyi.opscloud.domain.param.cloud.CloudImageParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudImageVO;
import com.baiyi.opscloud.facade.CloudImageFacade;
import com.baiyi.opscloud.service.cloud.OcCloudImageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/18 11:37 上午
 * @Version 1.0
 */
@Service
public class CloudImageFacadeImpl implements CloudImageFacade {

    @Resource
    private OcCloudImageService ocCloudImageService;

    @Override
    public DataTable<OcCloudImageVO.CloudImage> fuzzyQueryCloudImagePage(CloudImageParam.PageQuery pageQuery) {
        DataTable<OcCloudImage> table = ocCloudImageService.fuzzyQueryOcCloudImageByParam(pageQuery);
        List<OcCloudImageVO.CloudImage> page = BeanCopierUtils.copyListProperties(table.getData(), OcCloudImageVO.CloudImage.class);
        DataTable<OcCloudImageVO.CloudImage> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> syncCloudImageByKey(String key) {
        ICloudImage cloudImage = CloudImageFactory.getCloudImageByKey(key);
        return new BusinessWrapper<>(cloudImage.sync());
    }

    @Override
    public BusinessWrapper<Boolean> deleteCloudImageById(int id) {
        OcCloudImage ocCloudImage = ocCloudImageService.queryOcCloudImageById(id);
        if (ocCloudImage == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_IMAGE_NOT_EXIST);
        ocCloudImageService.deleteOcCloudImageById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> setCloudImageActive(int id) {
        OcCloudImage ocCloudImage = ocCloudImageService.queryOcCloudImageById(id);
        if (ocCloudImage == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_IMAGE_NOT_EXIST);
        ocCloudImage.setIsActive(ocCloudImage.getIsActive() == 0 ? 1 : 0);
        ocCloudImageService.updateOcCloudImage(ocCloudImage);
        return BusinessWrapper.SUCCESS;
    }
}
