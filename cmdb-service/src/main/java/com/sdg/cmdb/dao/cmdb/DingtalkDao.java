package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.dingtalk.DingtalkDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DingtalkDao {

    DingtalkDO getDingtalk(@Param("id") long id);

    DingtalkDO getDingtalkByType(@Param("dingtalkType") int dingtalkType);

    List<DingtalkDO> queryDingtalk();

}
