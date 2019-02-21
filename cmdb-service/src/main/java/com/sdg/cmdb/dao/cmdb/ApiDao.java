package com.sdg.cmdb.dao.cmdb;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface ApiDao {

    int checkApiToke(@Param("authToke") String authToke);

}
