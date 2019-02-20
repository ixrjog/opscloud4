package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.service.AnsibleServerService;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.util.CmdUtils;
import org.apache.commons.exec.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
public class AnsibleServerServiceImpl implements AnsibleServerService,InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AnsibleServerServiceImpl.class);
    //private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");

    @Resource
    private ConfigCenterService configCenterService;


    private HashMap<String, String> getwayConfigMap;

    private String ansibleBin;
    //private String ansibleHostsPath;

    public static final String ANSIBLE_MODULE_COPY = "copy";

    public static final String ANSIBLE_MODULE_SCRIPT = "script";

   @Override
    public String call(boolean isSudo, String hostPattern, String inventoryFile, String moduleName, String moduleArgs) {
        CommandLine c = new CommandLine(ansibleBin);
        c.addArgument(hostPattern);
        /**
         * (default=/etc/ansible/hosts
         */
        if (!StringUtils.isEmpty(inventoryFile)) {
            c.addArgument("-i");
            c.addArgument(inventoryFile);
        }
        if (isSudo)
            c.addArgument("-sudo");
        if (!StringUtils.isEmpty(moduleName)) {
            c.addArgument("-m");
            c.addArgument(moduleName);
        }
        if (!StringUtils.isEmpty(moduleArgs)) {
            c.addArgument("-a");
            c.addArgument(moduleArgs, false);
        }

        logger.info("ansible call : " + c.toString());

        String rt = CmdUtils.run(c);
        return rt;
    }


    /**
     * 初始化
     *
     * @return
     */
    private void init() {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }


}
