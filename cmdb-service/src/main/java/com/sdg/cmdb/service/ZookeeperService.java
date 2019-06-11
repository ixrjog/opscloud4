package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.dubbo.DubboProvider;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
public interface ZookeeperService {

    HashMap<String,DubboProvider> getProviderMap();

    List<DubboProvider> queryProviders(String dubbo);

    List<String> getDubboInfo(String path);

}
