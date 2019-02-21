package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.server.ServerStatisticsDO;

import java.util.List;

public interface ApiService {

    List<ServerStatisticsDO> getEcsStatus(String authToke);
}
