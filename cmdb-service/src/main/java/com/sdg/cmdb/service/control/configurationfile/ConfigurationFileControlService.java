package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.dao.cmdb.ConfigDao;
import com.sdg.cmdb.domain.config.ConfigFileDO;
import com.sdg.cmdb.domain.config.ConfigFileGroupDO;
import com.sdg.cmdb.service.configurationProcessor.AnsibleFileProcessorService;
import com.sdg.cmdb.service.configurationProcessor.ShadowsocksFileProcessorService;
import com.sdg.cmdb.util.IOUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2016/12/15.
 */
@Service
public class ConfigurationFileControlService {

    public final static String nameAnsible = "ansible";

    public final static String nameInterfaceCI = "interfaceCI";

    public final static String nameTomcatInstallConfig = "tomcatInstallConfig";

    public final static String nameGetway = "getway";

    public final static String nameShadowsocks = "shadowsocks";



    public final static String nameNginxLocationSupplier = "nginxLocationSupplier";



    @Resource
    private AnsibleFileProcessorService ansibleFileProcessorService;

    @Resource
    private InterfaceCIService interfaceCIService;

    @Resource
    private TomcatInstallConfigService tomcatInstallConfigService;

    @Autowired
    private ShadowsocksFileProcessorService shadowsocksFileProcessorService;

    @Resource
    private ConfigDao configDao;




    public static final String GROUP_ANSIBLE = "filegroup_ansible";

    public static final String GROUP_GETWAY = "filegroup_getway";

    public static final String GROUP_SHADOWSOCKS = "filegroup_ss";


    public String build(ConfigFileDO configFileDO) {
        ConfigFileGroupDO configFileGroupDO = configDao.getConfigFileGroupById(configFileDO.getFileGroupId());
        //System.err.println(configFileGroupDO);

        if (configFileGroupDO.getGroupName().equalsIgnoreCase(GROUP_ANSIBLE))
            return ansibleFileProcessorService.getConfig(configFileDO.getUseType(),true);

        if (configFileGroupDO.getGroupName().equalsIgnoreCase(GROUP_SHADOWSOCKS))
            return shadowsocksFileProcessorService.getConfig();

        return "";
    }

    /**
     * 按名称生成配置文件
     *
     * @param name
     * @param type
     */
    public String build(String name, int type) {
        switch (name) {
            case nameAnsible:
                return "";
            case nameInterfaceCI:
                return interfaceCIService.acqConfig();
            case nameTomcatInstallConfig:
                return tomcatInstallConfigService.acqConfig();
            case nameGetway:
                return "";
        }
        return new String();
    }

    /**
     * 写配置文件
     */


}
