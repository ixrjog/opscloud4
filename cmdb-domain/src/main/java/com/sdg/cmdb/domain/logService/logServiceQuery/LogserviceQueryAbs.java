package com.sdg.cmdb.domain.logService.logServiceQuery;


public interface LogserviceQueryAbs {

    String acqQueryBeginDate();

    String acqQueryEndDate();

    int acqToMinutes();


    // 0  nginx
    // 1  java
    int acqQueryType();

    LogServiceQueryCfg acqLogServiceQueryCfg();


}
