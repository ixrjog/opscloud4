package com.sdg.cmdb.domain.logService.logServiceQuery;


public interface LogServiceQuery {

    String acqQueryBeginDate();

    String acqQueryEndDate();

    int acqToMinutes();


    // 0  nginx
    // 1  java
    int acqQueryType();

    LogServiceQueryCfg acqLogServiceQueryCfg();


}
