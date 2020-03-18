package com.baiyi.opscloud.cloud.image.impl;

import com.baiyi.opscloud.cloud.image.ICloudImage;
import com.baiyi.opscloud.cloud.image.factory.CloudImageFactory;
import com.baiyi.opscloud.domain.generator.OcCloudImage;
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
        Map<String, OcCloudImage> cloudImageMap = getCloudImageMap(Lists.newArrayList());
        List<T> cloudImageList = getCloudImageList();
//        for (T instance : instanceList)
//            saveOcCloudServer(instance, cloudServerMap, pushName);

        return Boolean.TRUE;
    }


    protected Map<String, OcCloudImage> getCloudImageMap(List<OcCloudImage> cloudImageList) {
        if (CollectionUtils.isEmpty(cloudImageList))
            cloudImageList = ocCloudImageService.queryOcCloudImageByType(getCloudType());
        return cloudImageList.stream().collect(Collectors.toMap(OcCloudImage::getImageId, a -> a, (k1, k2) -> k1));
    }

    protected abstract List<T> getCloudImageList();


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
