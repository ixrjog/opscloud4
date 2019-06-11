package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.domain.dubbo.DubboProvider;
import com.sdg.cmdb.service.ZookeeperService;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class ZookeeperServiceImpl implements ZookeeperService {

    public static final String DUBBO = "/dubbo";
    public static final String PROVIDERS = "/providers";


    @Value("#{cmdb['zookeeper.test.server']}")
    private String zkTestServer;

    @Override
    public List<String> getDubboInfo(String path) {
        List<String> list = new ArrayList<String>();

        String zkHost[] = zkTestServer.split(":");

        ZkClient zkClient = new ZkClient(zkHost[0], Integer.valueOf(zkHost[1]));
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                log.error("parentPath:" + s + ", currentchilds:" + list);
            }
        });

        try {
            list = zkClient.getChildren(path);
            zkClient.close();
            //Thread.sleep(1000);
            return list;
        } catch (Exception e) {
            zkClient.close();
            log.error(String.valueOf(e));
        }

        return list;
    }

    public void test() {
        /**
         * providers
         * consumers
         * configurators
         * routers
         * /dubbo/com.ggj.platform.promotion.api.coupon.CouponAPI/providers
         * /dubbo/com.ggj.platform.promotion.api.coupon
         */
        List<String> apiList = getDubboInfo("/dubbo");
        for (String d : apiList)
            System.err.println(d);
    }

    @Override
    public HashMap<String, DubboProvider> getProviderMap() {
        List<String> dubboList = queryDubbo();
        HashMap<String, DubboProvider> map = new HashMap<>();
        for (String dubbo : dubboList) {
            log.info(dubbo);
            for (DubboProvider dp : queryProviders(dubbo))
                map.put(dp.getIp(), dp);
        }

        return map;
    }

    @Override
    public List<DubboProvider> queryProviders(String dubbo) {
        List<String> providersList = getDubboInfo(DUBBO + "/" + dubbo + PROVIDERS);
        List<DubboProvider> dubboProviders = new ArrayList<>();
        for (String p : providersList)
            dubboProviders.add(new DubboProvider(p));
        return dubboProviders;
    }

    /**
     * 查询所有dubbo接口
     *
     * @return
     */
    private List<String> queryDubbo() {
        List<String> apiList = getDubboInfo("/dubbo");
        return apiList;
    }

}
