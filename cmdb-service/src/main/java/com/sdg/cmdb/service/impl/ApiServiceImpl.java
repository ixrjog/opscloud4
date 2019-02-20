package com.sdg.cmdb.service.impl;


import com.sdg.cmdb.dao.cmdb.ApiDao;
import com.sdg.cmdb.domain.server.ServerStatisticsDO;
import com.sdg.cmdb.service.ApiService;
import com.sdg.cmdb.service.EcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {


    @Autowired
    private ApiDao apiDao;


    @Autowired
    private EcsService ecsService;

    @Override
    public List<ServerStatisticsDO> getEcsStatus(String authToke) {
        List<ServerStatisticsDO> list = new ArrayList<>();
        if (!checkToke(authToke))
            return list;
        list.add(ecsService.statistics());
        return list;
    }


    private boolean checkToke(String authToke) {
        if (apiDao.checkApiToke(authToke) == 0)
            return false;
        return true;
    }


}
