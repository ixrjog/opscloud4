package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.impl.ConfigServerGroupServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 2017/3/9.
 */
@Service
public class NginxLocationService extends ConfigurationFileControlAbs {


    public String acqConfig() {
        return acqConfig(ServerDO.EnvTypeEnum.daily.getCode());
    }

    public  String acqDomain(){
        return "www.52shangou.com";
    }

    @Override
    protected boolean isBuildLocation(ServerGroupDO serverGroupDO){
        return  configServerGroupService.isbuildLocation(serverGroupDO);
    }


    /**
     * 按类型build
     * 0: daily
     *
     * @param type
     */

    public String acqConfig(int type) {
        int envCode = type;
        return buildLocation(envCode);
    }


}
