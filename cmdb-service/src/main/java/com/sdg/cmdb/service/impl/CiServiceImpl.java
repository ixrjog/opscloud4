package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.CiDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.ci.CiDeployServerVersionDO;
import com.sdg.cmdb.domain.ci.CiDeployServerVersionVO;
import com.sdg.cmdb.domain.ci.CiDeployStatisticsDO;
import com.sdg.cmdb.domain.ci.ciStatus.CiDeployVO;
import com.sdg.cmdb.domain.ci.ciStatus.CiStatusVO;
import com.sdg.cmdb.domain.ip.IPDetailDO;
import com.sdg.cmdb.domain.ip.IPDetailVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerVO;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.service.control.configurationfile.AnsibleHostService;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.TimeViewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liangjian on 2017/2/17.
 */
@Service
public class CiServiceImpl implements CiService {

    private static final Logger logger = LoggerFactory.getLogger(CiServiceImpl.class);

    @Resource
    private AuthService authService;

    @Resource
    private CiDao ciDao;

    @Resource
    private ServerDao serverDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private IPService ipService;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private AnsibleHostService ansibleHostService;

    @Resource
    private DingtalkService dingtalkService;

    @Override
    public TableVO<List<CiDeployStatisticsDO>> getCiDeployStatisticsPage(String project, int status, int deployType, int bambooDeployRollback,
                                                                         int errorCode, int page, int length) {
        long size = ciDao.getCiDeployStatisticsSize(project, status, deployType, bambooDeployRollback,
                errorCode);
        List<CiDeployStatisticsDO> list = ciDao.getCiDeployStatisticsPage(project, status, deployType, bambooDeployRollback,
                errorCode, page * length, length);

        for (CiDeployStatisticsDO ciDeployStatisticsDO : list) {
            UserDO userDO = authService.getUserByName(ciDeployStatisticsDO.getBambooManualBuildTriggerReasonUserName());
            if (userDO == null) continue;
            UserVO userVO = new UserVO();
            userVO.setDisplayName(userDO.getDisplayName());
            userVO.setMail(userDO.getMail());
            if (!StringUtils.isEmpty(userDO.getMobile()))
                userVO.setMobile(userDO.getMobile());
            ciDeployStatisticsDO.setUserVO(userVO);
        }

        return new TableVO<>(size, list);

    }

    @Override
    public TableVO<List<CiDeployServerVersionVO>> getServerPage(long serverGroupId, String serverName, int envType, String queryIp, int page, int length) {
        List<Long> groupFilter = authService.getUserGroupIds(SessionUtils.getUsername());
        long size = serverDao.getServerSize(groupFilter, serverGroupId, serverName, ServerGroupDO.UseTypeEnum.webservice.getCode(), envType, queryIp);
        List<ServerDO> list = serverDao.getServerPage(groupFilter, serverGroupId, serverName, ServerGroupDO.UseTypeEnum.webservice.getCode(), envType, queryIp, page * length, length);
        List<CiDeployServerVersionVO> voList = new ArrayList<>();
        for (ServerDO serverDO : list) {
            ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupById(serverDO.getServerGroupId());
            IPDetailVO publicIP = ipService.getIPDetail(new IPDetailDO(serverDO.getPublicIpId()));
            IPDetailVO insideIP = ipService.getIPDetail(new IPDetailDO(serverDO.getInsideIpId()));
            ServerVO serverVO = new ServerVO(serverDO, serverGroupDO, publicIP, insideIP);
            CiDeployServerVersionDO ciDeployServerVersionDO = ciDao.getCiDeployServerVersionByServerId(serverDO.getId());
            CiDeployStatisticsDO ciDeployStatisticsDO = new CiDeployStatisticsDO();
            if (ciDeployServerVersionDO != null) {
                ciDeployStatisticsDO = ciDao.getCiDeployStatisticsById(ciDeployServerVersionDO.getCiDeployStatisticsId());
            }
            CiDeployServerVersionVO ciDeployServerVersionVO = new CiDeployServerVersionVO(ciDeployServerVersionDO, ciDeployStatisticsDO, serverVO);

            // 显示用户信息
            if (!StringUtils.isEmpty(ciDeployStatisticsDO.getBambooManualBuildTriggerReasonUserName())) {
                UserDO userDO = authService.getUserByName(ciDeployStatisticsDO.getBambooManualBuildTriggerReasonUserName());
                if (userDO != null) {
                    UserVO userVO = new UserVO();
                    userVO.setDisplayName(userDO.getDisplayName());
                    userVO.setMail(userDO.getMail());
                    if (!StringUtils.isEmpty(userDO.getMobile()))
                        userVO.setMobile(userDO.getMobile());
                    ciDeployServerVersionVO.setUserVO(userVO);
                }
            }
            voList.add(ciDeployServerVersionVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public void doCiDeployStatistics(CiDeployStatisticsDO ciDeployStatisticsDO) {
        //部署统计
        CiDeployStatisticsDO ciDO = ciDao.getCiDeployStatisticsByDeployId(ciDeployStatisticsDO.getDeployId());
        if (ciDO == null) {
            ciDao.addCiDeployStatistics(ciDeployStatisticsDO);
        } else {
            ciDO.setStatus(ciDeployStatisticsDO.getStatus());
            ciDO.setErrorCode(ciDeployStatisticsDO.getErrorCode());
            ciDao.updateCiDeployStatistics(ciDO);
        }
        doCiDeployServerVersion(ciDO);
        try {
            dingtalkService.sendCiDeployMsg(ciDO);
        } catch (Exception e) {
            logger.error("CI钉钉消息推送失败!");
        }
    }

    private void doCiDeployServerVersion(CiDeployStatisticsDO ciDeployStatisticsDO) {
        if (ciDeployStatisticsDO == null) return;
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_" + ciDeployStatisticsDO.getProjectName());
        if (serverGroupDO == null) return;
        List<ServerDO> servers = ansibleHostService.queryServerGroupByCiGroupName(serverGroupDO, ciDeployStatisticsDO.getGroupName());
        for (ServerDO serverDO : servers) {
            CiDeployServerVersionDO ciDeployServerVersionDO = ciDao.getCiDeployServerVersionByServerId(serverDO.getId());
            if (ciDeployServerVersionDO == null) {
                ciDeployServerVersionDO = new CiDeployServerVersionDO(serverDO.getId(), ciDeployStatisticsDO.getId());
                ciDao.addCiDeployServerVersion(ciDeployServerVersionDO);
            } else {
                ciDeployServerVersionDO.setCiDeployStatisticsId(ciDeployStatisticsDO.getId());
                ciDao.updateCiDeployServerVersion(ciDeployServerVersionDO);
            }
        }
    }

    @Override
    public CiStatusVO ciStatus() {
        CiStatusVO statusVO = new CiStatusVO();

        statusVO.setTopProjectList(ciDao.statusCiDeployProject());
        statusVO.setTopUserList(ciDao.statusCiDeployUser());
        statusVO.setTopRepoList(ciDao.statusCiRepo());

        statusVO.setDeployedCnt(ciDao.getCiDeployedCnt());
        statusVO.setProjectCnt(ciDao.getCiProjectCnt());

        List<CiDeployVO> ciDeployList = ciDao.statusCiDeploy();
        for (CiDeployVO ciDeployVO : ciDeployList) {
            ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_" + ciDeployVO.getProjectName());
            if (serverGroupDO != null) {
                List<ServerDO> servers = ansibleHostService.queryServerGroupByCiGroupName(serverGroupDO, ciDeployVO.getGroupName());
                ciDeployVO.setServers(servers);
            }

            try {
                String gmtDeploy;
                if (StringUtils.isEmpty(ciDeployVO.getGmtModify())) {
                    gmtDeploy = ciDeployVO.getGmtCreate();
                } else {
                    gmtDeploy = ciDeployVO.getGmtModify();
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
                Date createDate = format.parse(gmtDeploy);
                ciDeployVO.setTimeView(TimeViewUtils.format(createDate));
            } catch (Exception e) {
            }
            ciDeployVO.setGroupName(ciDeployVO.getGroupName().replace(ciDeployVO.getProjectName() + "-", ""));

        }
        statusVO.setCiDeployList(ciDeployList);

        // List<ServerDO> servers = ansibleHostService.queryServerGroupByCiGroupName(serverGroupDO, ciDeployStatisticsDO.getGroupName());

        return statusVO;
    }


}
