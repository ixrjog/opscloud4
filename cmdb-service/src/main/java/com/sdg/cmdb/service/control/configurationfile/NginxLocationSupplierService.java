package com.sdg.cmdb.service.control.configurationfile;


import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.impl.ConfigServerGroupServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NginxLocationSupplierService extends ConfigurationFileControlAbs {



    public  String acqDomain(){
        //供应商
        return "caihaohuo.cn";
    }


    public String acqConfig(int type) {
        int envCode = type;
        return buildLocation(envCode);
    }

    @Override
    protected boolean isBuildLocation(ServerGroupDO serverGroupDO){
        return configServerGroupService.isBuildSupplierLocation(serverGroupDO);
    }

}
