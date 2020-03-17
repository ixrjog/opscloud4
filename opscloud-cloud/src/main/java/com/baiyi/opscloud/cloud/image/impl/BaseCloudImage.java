package com.baiyi.opscloud.cloud.image.impl;

import com.baiyi.opscloud.cloud.image.ICloudImage;
import com.baiyi.opscloud.cloud.image.factory.CloudImageFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2020/3/17 6:36 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseCloudImage<T> implements InitializingBean, ICloudImage {

    @Override
    public Boolean sync() {
//        Map<String, OcCloudImage> cloudImageMap = getCloudImageMap(Lists.newArrayList());
//        List<T> instanceList = getInstanceList();
//        for (T instance : instanceList)
//            saveOcCloudServer(instance, cloudServerMap, pushName);

        return Boolean.TRUE;
    }


//    protected Map<String, OcCloudImage> getCloudImageMap(List<OcCloudImage> cloudImageList) {
//        if (CollectionUtils.isEmpty(cloudImageList))
//            cloudServerList = ocCloudServerService.queryOcCloudServerByType(getCloudServerType());
//        return cloudServerList.stream().collect(Collectors.toMap(OcCloudServer::getInstanceId, a -> a, (k1, k2) -> k1));
//    }


    /**
     * 取云服务器类型
     *
     * @return
     */
    abstract protected int getCloudImageType();

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
