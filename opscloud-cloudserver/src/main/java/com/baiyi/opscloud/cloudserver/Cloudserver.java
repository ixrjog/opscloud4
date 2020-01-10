package com.baiyi.opscloud.cloudserver;

import com.baiyi.opscloud.cloudserver.factory.CloudserverFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/9 3:39 下午
 * @Version 1.0
 */
@Slf4j
public abstract class Cloudserver<T> implements InitializingBean {


    /**
     * 同步接口
     *
     * @return
     */
    public Boolean sync() {
        return Boolean.TRUE;
    }

//    public Boolean sync(boolean isPushInstanceName) {
//        List<T> instanceList = getInstanceList();
//        for (T instance : instanceList)
//            updateCloudServerByInstance(instance, map, pushInstanceName);
//        // 标记为云服务器不存在
//        updateServerStatus(map, CLOUDSERVER_STATUS_OFFLINE);
//        checkCloudServer();
//        return new BusinessWrapper<Boolean>(true);
//    }

    /**
     * List -> Map
     * 需要注意的是：
     * toMap 如果集合对象有重复的key，会报错Duplicate key ....
     * apple1,apple12的id都为1。
     * 可以用 (k1,k2)->k1 来设置，如果有重复的key,则保留key1,舍弃key2
     */
//    private Map<String, T> getInstanceMap(List<T> instanceList) {
//
//        Map<String, T> instanceMap = instanceList.stream().collect(Collectors.toMap(T::getId, a -> a, (k1, k2) -> k1));
//        // Map<Integer, Apple> appleMap = appleList.stream().collect(Collectors.toMap(Apple::getId, a -> a,(k1,k2)->k1));
//    }
    abstract protected List<T> getInstanceList();

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
        CloudserverFactory.register(this);
    }

}
