package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.nginx.*;

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
     * 生产配置文件
     *
     * @param envFileId
     * @return
     */
    BusinessWrapper<Boolean> buildEnvFile(long envFileId);

    BusinessWrapper<Boolean> addServerGroup(long vhostId, long serverGroupId);

    BusinessWrapper<Boolean> delServerGroup(long id);

    List<VhostServerGroupVO> queryServerGroup(long vhostId);
}
