package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.dao.cmdb.ConfigDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.ConfigServerGroupService;
import com.sdg.cmdb.service.ConfigService;
import com.sdg.cmdb.service.impl.ConfigServerGroupServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liangjian on 2016/12/15.
 */
public abstract class ConfigurationFileControlAbs {

    @Resource
    protected ServerDao serverDao;

    @Resource
    protected ServerGroupDao serverGroupDao;

    @Resource
    protected ConfigDao configDao;

    @Resource
    protected UserDao userDao;

    @Resource
    protected ConfigServerGroupService configServerGroupService;

    @Resource
    protected ConfigService configService;



    //文件头信息
    //protected String headInfo;

    protected String getHeadInfo() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        String headInfo = "# Created by cmdb on " + fastDateFormat.format(new Date()) + "\n\n";
        return headInfo;
    }

    /**
     * 取所有的webservice组信息
     */
    protected List<ServerGroupDO> acqServerGroupByWebservice() {
        List<ServerGroupDO> list = serverGroupDao.queryServerGroup();
        List<ServerGroupDO> result = new ArrayList<ServerGroupDO>();
        for (ServerGroupDO serverGroupDO : list) {
            if (serverGroupDO.getUseType() == ServerGroupDO.UseTypeEnum.webservice.getCode())
                result.add(serverGroupDO);
        }
        return result;
    }

    /**
     * 按环境类型取服务器
     *
     * @param serverGroupDO
     * @param envType
     * @return
     */
    protected List<ServerDO> acqServerByGroup(ServerGroupDO serverGroupDO, int envType) {
        List<ServerDO> list = serverDao.acqServersByGroupId(serverGroupDO.getId());
        List<ServerDO> result = new ArrayList<ServerDO>();
        for (ServerDO serverDO : list) {
            if (serverDO.getEnvType() == envType)
                result.add(serverDO);
        }
        return result;
    }

    protected List<ServerDO> acqServerByGroup(ServerGroupDO serverGroupDO) {
        List<ServerDO> list = serverDao.acqServersByGroupId(serverGroupDO.getId());
        return list;
    }

    /**
     * 获取location中的扩展参数并格式化
     *
     * @param serverGroupDO
     * @return
     */
    protected String acqLocationParam(ServerGroupDO serverGroupDO) {

        String sourceParams = configServerGroupService.queryNginxLocationParam(serverGroupDO);
        if (StringUtils.isEmpty(sourceParams)) return "";

        String[] params = sourceParams.split(";");
        String result = "";

        for (String param : params) {
            result += ConfigServerGroupServiceImpl.nginx_locaton_indent + param + ";\n";
        }
        return result;
    }


    protected String buildLocation(int envCode){
        String result = "";
        List<String> projects = new ArrayList<String>();
        //ServerDO.EnvTypeEnum.daily.getCode();
        List<ServerGroupDO> listServerGroupDO = getServerGroup(envCode);
        for (ServerGroupDO serverGroupDO : listServerGroupDO) {
            List<ServerDO> listServerDO = acqServerByGroup(serverGroupDO, envCode);
            // 无服务器
            if (listServerDO.size() == 0) {
                //判断 isGrayEqProd
                if (!configServerGroupService.isGrayEqProd(serverGroupDO))
                    continue;
            }
            // NGINX_LOCATION_MANAGE_BUILD = false
            if (!isBuildLocation(serverGroupDO)) continue;
            String l = buildLocation(serverGroupDO, envCode, projects);
            if (l != null)
                result += l + "\n";
        }
        return result;
    }

    /**
     * 是否构建location
     * @return
     */
    protected  boolean isBuildLocation(ServerGroupDO serverGroupDO){
        return false;
    }


    protected String buildLocation(ServerGroupDO serverGroupDO, int envCode, List<String> projects) {
        String projectName = configServerGroupService.queryProjectName(serverGroupDO);
        if (projectName == null) return null;
        String result;
        if (serverGroupDO.getContent() != null && !serverGroupDO.getContent().isEmpty()) {
            result = "# " + serverGroupDO.getContent() + "\n";
        } else {
            result = "# " + serverGroupDO.getName() + "\n";
        }

        result += "location " + configServerGroupService.queryNginxUrl(serverGroupDO) + "/ {\n";

        // 增加location扩展参数
        result += acqLocationParam(serverGroupDO);

        String nginxLocationLimitReq = configServerGroupService.queryNginxLocationLimitReq(serverGroupDO);
        if (nginxLocationLimitReq != null)
            result += "    " + nginxLocationLimitReq + "\n";
        result += configServerGroupService.queryNginxProxyPass(serverGroupDO, envCode);

        result += "    include /usr/local/nginx/conf/vhost/proxy_default.conf;\n";
        result += "}\n\n";
        projects.add(serverGroupDO.getName());
        return result;
    }



    protected List<ServerGroupDO> getServerGroup(int envType) {
        List<ServerGroupDO> list = acqServerGroupByWebservice();
        return list;
    }


}
