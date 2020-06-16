package com.baiyi.opscloud.cloud.image.impl;

import com.baiyi.opscloud.cloud.account.CloudAccount;
import com.baiyi.opscloud.cloud.image.ICloudImage;
import com.baiyi.opscloud.cloud.image.factory.CloudImageFactory;
import com.baiyi.opscloud.common.util.JSONUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudImage;
import com.baiyi.opscloud.service.cloud.OcCloudImageService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/3/17 6:36 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseCloudImage<T> implements InitializingBean, ICloudImage {

    @Resource
    private OcCloudImageService ocCloudImageService;

    @Override
    public Boolean sync() {
        CloudAccount cloudAccount = getCloudAccount();
        if (cloudAccount == null) return Boolean.FALSE;
        Map<String, OcCloudImage> cloudImageMap = getCloudImageMap(Lists.newArrayList());
       getCloudImageList().forEach(i->saveOcCloudImage(cloudAccount, i, cloudImageMap));
        return Boolean.TRUE;
    }

    protected void saveOcCloudImage(CloudAccount account, T cloudImage, Map<String, OcCloudImage> map) {
        try {
            String imageId = getImageId(cloudImage);
            if (map.containsKey(imageId)) {
                map.remove(imageId);
            } else {
                addOcCloudImage(getCloudImage(account, cloudImage));
            }
        } catch (Exception e) {
        }
    }

    private void addOcCloudImage(OcCloudImage ocCloudImage) {
        ocCloudImageService.addOcCloudImage(ocCloudImage);
    }

    protected abstract CloudAccount getCloudAccount();

    protected Map<String, OcCloudImage> getCloudImageMap(List<OcCloudImage> cloudImageList) {
        if (CollectionUtils.isEmpty(cloudImageList))
            cloudImageList = ocCloudImageService.queryOcCloudImageByType(getCloudType());
        return cloudImageList.stream().collect(Collectors.toMap(OcCloudImage::getImageId, a -> a, (k1, k2) -> k1));
    }

    protected abstract List<T> getCloudImageList();

    protected abstract String getImageId(T cloudImage) throws Exception;

    protected abstract OcCloudImage getCloudImage(CloudAccount account, T cloudImage);

    protected String getCloudImageDetail(Object cloudImage) {
        return JSONUtils.writeValueAsString(cloudImage);
    }

    /**
     * 取云服务器类型
     *
     * @return
     */
    abstract protected int getCloudType();

    @Override
    public String getKey() {
        return this.getClass().getSimpleName();
    }

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        CloudImageFactory.register(this);
    }


}
