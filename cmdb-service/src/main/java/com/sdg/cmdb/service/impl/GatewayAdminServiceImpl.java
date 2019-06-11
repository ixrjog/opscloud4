package com.sdg.cmdb.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdg.cmdb.dao.cmdb.GatewayAdminDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.gatewayAdmin.*;
import com.sdg.cmdb.domain.server.EnvType;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.ConfigServerGroupService;
import com.sdg.cmdb.service.GatewayAdminService;
import com.sdg.cmdb.service.ServerGroupService;
import com.sdg.cmdb.service.configurationProcessor.GatewayAdminProcessorService;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class GatewayAdminServiceImpl implements GatewayAdminService {

    @Autowired
    private GatewayAdminProcessorService gatewayAdminProcessorService;
    @Autowired
    private ServerGroupService serverGroupService;
    @Autowired
    private ConfigServerGroupService configServerGroupService;
    @Autowired
    private GatewayAdminDao gatewayAdminDao;

    public static String REQUEST_PATH_APP_SET = "/api/ops/app/set";
    public static String REQUEST_PATH_APPSERVER_SET = "/api/ops/app-server/set";
    public static String REQUEST_PATH_APPSERVER_LIST = "/api/ops/app-server/list";

    @Value(value = "${gateway.admin.test.host}")
    private String gatewayAdminTestHost;
    @Value(value = "${gateway.admin.test.ak}")
    private String gatewayAdminTestAk;

    @Value(value = "${gateway.admin.pre.host}")
    private String gatewayAdminPreHost;
    @Value(value = "${gateway.admin.pre.ak}")
    private String gatewayAdminPreAk;

    @Value(value = "${gateway.admin.prod.host}")
    private String gatewayAdminProdHost;
    @Value(value = "${gateway.admin.prod.ak}")
    private String gatewayAdminProdAk;

    @Override
    public TableVO<List<GatewayAdminDO>> queryGatewayAdminPage(String serverGroupName, String appName, int page, int length) {
        long size = gatewayAdminDao.getGatewayAdminSize(serverGroupName, appName);
        List<GatewayAdminDO> list = gatewayAdminDao.getGatewayAdminPage(serverGroupName, appName, page * length, length);
        return new TableVO<>(size, list);
    }

    @Override
    public BusinessWrapper<Boolean> syncGatewayAdmin() {
        List<ServerGroupDO> serverGroupList = serverGroupService.getServerGroupsAll();
        for (ServerGroupDO serverGroupDO : serverGroupList) {
            String appName = configServerGroupService.queryGatewayAdminAppName(serverGroupDO);
            if (!StringUtils.isEmpty(appName))
                saveGatewayAdmin(appName, serverGroupDO);
        }
        return new BusinessWrapper<Boolean>(true);
    }

    private boolean saveGatewayAdmin(String appName, ServerGroupDO serverGroupDO) {
        GatewayAdminDO gatewayAdminDO = gatewayAdminDao.getGatewayAdminByAppName(appName);
        if (gatewayAdminDO != null) {
            gatewayAdminDO.setServerGroupName(serverGroupDO.getName());
            gatewayAdminDO.setAppName(appName);
        } else {
            gatewayAdminDO = new GatewayAdminDO(appName, serverGroupDO);
        }
        gatewayAdminDO.setRoutePath(configServerGroupService.queryGatewayAdminRoutePath(serverGroupDO));
        gatewayAdminDO.setHealthCheckPath(configServerGroupService.queryGatewayAdminAppHealthCheckPath(serverGroupDO));
        gatewayAdminDO.setServiceTest(configServerGroupService.queryGatewayAdminAppServiceByKey(serverGroupDO, GatewayAdminProcessorService.GETWAYADMIN_APP_SERVICE_TEST));
        gatewayAdminDO.setServicePre(configServerGroupService.queryGatewayAdminAppServiceByKey(serverGroupDO, GatewayAdminProcessorService.GETWAYADMIN_APP_SERVICE_PRE));
        return saveGatewayAdmin(gatewayAdminDO);
    }

    private boolean saveGatewayAdmin(GatewayAdminDO gatewayAdminDO) {
        try {
            if (gatewayAdminDO.getId() == 0) {
                gatewayAdminDao.addGatewayAdmin(gatewayAdminDO);
            } else {
                gatewayAdminDao.updateGatewayAdmin(gatewayAdminDO);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public BusinessWrapper<Boolean> appSet(long id, int envType) {
        GatewayAdminDO gatewayAdminDO = gatewayAdminDao.getGatewayAdmin(id);
        SetResult sr = setApp(gatewayAdminDO.getServerGroupId(), envType);
        updateGatewayAdmin(gatewayAdminDO, sr, envType);
        if (sr.isData())
            return new BusinessWrapper<Boolean>(true);
        return new BusinessWrapper<Boolean>(ErrorCode.gatewayAdminSetError);
    }

    @Override
    public AppSetVO preview(long id, int envType) {
        GatewayAdminDO gatewayAdminDO = gatewayAdminDao.getGatewayAdmin(id);
        AppSet appSet = gatewayAdminProcessorService.getAppSet(gatewayAdminDO.getServerGroupId());
        AppServerSet appServerSet = gatewayAdminProcessorService.getAppServerSet(gatewayAdminDO.getServerGroupId(), envType);
        try {
            ObjectMapper mapper1 = new ObjectMapper();
            Object objAppSet = mapper1.readValue(appSet.toString(), Object.class);
            String appSetJson = mapper1.writerWithDefaultPrettyPrinter().writeValueAsString(objAppSet);

            ObjectMapper mapper2 = new ObjectMapper();
            Object objAppServerSet = mapper2.readValue(appServerSet.toString(), Object.class);
            String appServerSetJson = mapper2.writerWithDefaultPrettyPrinter().writeValueAsString(objAppServerSet);
            AppSetVO appSetVO = new AppSetVO(gatewayAdminDO, appSetJson, appServerSetJson);
            return appSetVO;
        } catch (Exception e) {
            e.printStackTrace();
            return new AppSetVO();
        }
    }

    @Override
    public BusinessWrapper<Boolean> batchSet() {
        List<GatewayAdminDO> list = gatewayAdminDao.getGatewayAdminAll();
        for (GatewayAdminDO gatewayAdminDO : list) {
            appSet(gatewayAdminDO.getId(), EnvType.EnvTypeEnum.test.getCode());
            appServerSet(gatewayAdminDO.getId(), EnvType.EnvTypeEnum.test.getCode());
            appSet(gatewayAdminDO.getId(), EnvType.EnvTypeEnum.pre.getCode());
            appServerSet(gatewayAdminDO.getId(), EnvType.EnvTypeEnum.pre.getCode());
            appSet(gatewayAdminDO.getId(), EnvType.EnvTypeEnum.prod.getCode());
            appServerSet(gatewayAdminDO.getId(), EnvType.EnvTypeEnum.prod.getCode());
        }
        return new BusinessWrapper<Boolean>(true);
    }

    @Override
    public BusinessWrapper<Boolean> appServerSet(long id, int envType) {
        GatewayAdminDO gatewayAdminDO = gatewayAdminDao.getGatewayAdmin(id);
        SetResult sr = setAppServer(gatewayAdminDO.getServerGroupId(), envType);
        updateGatewayAdmin(gatewayAdminDO, sr, envType);
        if (sr.isSuccess())
            return new BusinessWrapper<Boolean>(true);
        return new BusinessWrapper<Boolean>(ErrorCode.gatewayAdminSetError);
    }

    @Override
    public BusinessWrapper<Boolean> delGatewayadmin(long id) {
        try {
            gatewayAdminDao.delGatewayAdmin(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    private void updateGatewayAdmin(GatewayAdminDO gatewayAdminDO, SetResult result, int envType) {
        if (envType == EnvType.EnvTypeEnum.test.getCode()) {
            gatewayAdminDO.setTestSuccess(result.isSuccess());
            gatewayAdminDao.updateGatewayAdmin(gatewayAdminDO);
            return;
        }
        if (envType == EnvType.EnvTypeEnum.pre.getCode()) {
            gatewayAdminDO.setPreSuccess(result.isSuccess());
            gatewayAdminDao.updateGatewayAdmin(gatewayAdminDO);
            return;
        }
        if (envType == EnvType.EnvTypeEnum.prod.getCode()) {
            gatewayAdminDO.setProdSuccess(result.isSuccess());
            gatewayAdminDao.updateGatewayAdmin(gatewayAdminDO);
            return;
        }
    }

    private SetResult doSet(String webHook, Object body) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(webHook);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity se = new StringEntity(body.toString(), "utf-8");
        httppost.setEntity(se);
        try {
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                Gson gson = new GsonBuilder().create();
                SetResult sr = gson.fromJson(result, SetResult.class);
                return sr;
            }
        } catch (Exception e) {
           // e.printStackTrace();
        }
        return new SetResult();
    }

    @Override
    public String appServerList(long id, int envType) {
        GatewayAdminDO gatewayAdminDO = gatewayAdminDao.getGatewayAdmin(id);
        String webHook = getWebhookByList(envType, REQUEST_PATH_APPSERVER_LIST, gatewayAdminDO.getAppName());
        String result = doList(webHook);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object objAppServerList = mapper.readValue(result, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objAppServerList);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }


    private String doList(String webHook) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(webHook);
        try {
            HttpResponse response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private SetResult setApp(long severGroupId, int envType) {
        AppSet appSet = gatewayAdminProcessorService.getAppSet(severGroupId);
        if (appSet == null)
            return new SetResult();
        String webHook = getWebhookBySet(envType, REQUEST_PATH_APP_SET);
        return doSet(webHook, appSet);
    }

    private SetResult setAppServer(long severGroupId, int envType) {
        AppServerSet appServerSet = gatewayAdminProcessorService.getAppServerSet(severGroupId, envType);
        if (appServerSet == null)
            return new SetResult();
        String webHook = getWebhookBySet(envType, REQUEST_PATH_APPSERVER_SET);
        return doSet(webHook, appServerSet);
    }

    private String getWebhookBySet(int envType, String requestPath) {
        return getWebhookBase(envType, requestPath);
    }


    private String getWebhookByList(int envType, String requestPath, String appName) {
        String base = getWebhookBase(envType, requestPath);
        return base + "&appName=" + appName;
    }

    private String getWebhookBase(int envType, String requestPath) {
        if (envType == EnvType.EnvTypeEnum.test.getCode()) {
            return gatewayAdminTestHost + requestPath + "?_ak=" + gatewayAdminTestAk;
        }
        if (envType == EnvType.EnvTypeEnum.pre.getCode()) {
            return gatewayAdminPreHost + requestPath + "?_ak=" + gatewayAdminPreAk;
        }
        if (envType == EnvType.EnvTypeEnum.prod.getCode()) {
            return gatewayAdminProdHost + requestPath + "?_ak=" + gatewayAdminProdAk;
        }
        return "";
    }

}
