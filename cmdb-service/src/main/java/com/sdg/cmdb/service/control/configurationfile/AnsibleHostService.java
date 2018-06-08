package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.util.List;
import java.util.Map;

/**
 * Created by liangjian on 2017/4/12.
 */
public interface AnsibleHostService {


    /**
     * 按持续集成分组名称查询主机组
     *
     * @param serverGroupDO
     * @param ciGroupName
     * @return
     */
    List<ServerDO> queryServerGroupByCiGroupName(ServerGroupDO serverGroupDO, String ciGroupName);


    String acqHostsAllCfg();


    String acqHostsCfgByUseType(int useType);

}
