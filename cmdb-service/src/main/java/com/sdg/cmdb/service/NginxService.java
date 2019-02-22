package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ansibleTask.PlaybookLogVO;
import com.sdg.cmdb.domain.nginx.*;
import com.sdg.cmdb.service.configurationProcessor.NginxFileProcessorService;

import java.util.List;

public interface NginxService {

    TableVO<List<VhostVO>> getVhostPage(String serverName, int page, int length);

    BusinessWrapper<Boolean>  delVhost(long id);

    VhostVO getVhost(long id);

    BusinessWrapper<Boolean> saveVhost(VhostVO vhostVO);

    BusinessWrapper<Boolean> saveVhostEnv(VhostEnvDO vhostEnvDO);

    BusinessWrapper<Boolean> delVhostEnv(long id);

    BusinessWrapper<Boolean> saveEnvFile(EnvFileDO envFileDO);

    /**
     * 自动化配置
     */
   // void auto(long serverId);

   // void auto(long serverGroupId,int envType);

    /**
     * 预览本地文件
     *
     * @param envFileId
     * @param type
     * @return
     */
    BusinessWrapper<NginxFile> launchEnvFile(long envFileId, int type);

    /**
     * build配置文件(强制)
     *
     * @param envFileId
     * @return
     */
    BusinessWrapper<Boolean> buildEnvFile(long envFileId);

    /**
     * 判断 autoBuild 是否自动构建
     * @param envFileId
     * @param auto
     * @return
     */
    BusinessWrapper<Boolean> buildEnvFile(long envFileId,boolean auto);

    BusinessWrapper<Boolean> addServerGroup(long vhostId, long serverGroupId);

    BusinessWrapper<Boolean> delServerGroup(long id);

    List<VhostServerGroupVO> queryServerGroup(long vhostId);


    List<NginxPlaybookVO> getPlaybookPage();

    BusinessWrapper<Boolean>  savePlaybook(NginxPlaybookDO nginxPlaybookDO);

    PlaybookLogVO doPlaybook(long id, int doType);

    BusinessWrapper<Boolean>  delPlaybook(long id);
}
