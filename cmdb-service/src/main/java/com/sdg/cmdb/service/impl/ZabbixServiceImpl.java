package com.sdg.cmdb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.dao.cmdb.*;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.ci.CiDeployStatisticsDO;
import com.sdg.cmdb.domain.config.ConfigPropertyDO;
import com.sdg.cmdb.domain.config.ServerGroupPropertiesDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.ZabbixItemEnum;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.zabbix.*;
import com.sdg.cmdb.plugin.cache.CacheZabbixService;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by liangjian on 2016/12/16.
 */
@Service
public class ZabbixServiceImpl implements ZabbixService, InitializingBean {

    @Resource
    private ConfigCenterService configCenterService;

    @Resource
    private CacheZabbixService cacheZabbixService;

    @Resource
    private ZabbixHistoryService zabbixHistoryService;


    @Resource
    private ZabbixServerDao zabbixDao;

    @Resource
    private SchedulerManager schedulerManager;


    public static final String ZABBIX_SERVER_DEFAULT_NAME = "Zabbix server";

    /**
     * 不处理的账户
     */
    private String[] excludeUsers = {"Admin", "guest", "zabbix_admin", "liangjian"};

    private static final Logger logger = LoggerFactory.getLogger(ZabbixServiceImpl.class);

    public static final String zabbix_host_monitor_public_ip = "ZABBIX_HOST_MONITOR_PUBLIC_IP";

    public static final String zabbix_proxy_id = "ZABBIX_PROXY_ID";

    public static final String zabbix_proxy_name = "ZABBIX_PROXY_NAME";

    private CloseableHttpClient httpClient;

    private URI uri;

    private volatile String auth;

    @Resource
    protected ServerDao serverDao;

    @Resource
    protected ConfigDao configDao;

    @Resource
    protected UserDao userDao;

    @Resource
    protected CiDao ciDao;

    @Resource
    protected CiService ciService;

    //停用主机监控
    public static final int hostStatusDisable = 1;

    //启用主机监控
    public static final int hostStatusEnable = 0;

    //主机监控存在
    public static final int isMonitor = 1;

    //主机监控不存在
    public static final int noMonitor = 0;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    protected ConfigServerGroupService configServerGroupService;

    @Resource
    private KeyboxDao keyboxDao;

    private void setUrl(String url) {
        try {
            uri = new URI(url.trim());
        } catch (URISyntaxException e) {
            throw new RuntimeException("zabbix url invalid", e);
        }
    }

    private void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private HashMap<String, String> configMap;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.ZABBIX.getItemKey());
    }


    /**
     * 初始化
     *
     * @return
     */
    private void init() {
        HashMap<String, String> configMap = acqConifMap();
        String zabbixApiUrl = configMap.get(ZabbixItemEnum.ZABBIX_API_URL.getItemKey());
        String zabbixApiUser = configMap.get(ZabbixItemEnum.ZABBIX_API_USER.getItemKey());
        String zabbixAipPasswd = configMap.get(ZabbixItemEnum.ZABBIX_API_PASSWD.getItemKey());

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5 * 1000).setConnectionRequestTimeout(5 * 1000)
                .setSocketTimeout(5 * 1000).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig).build();
        setUrl(zabbixApiUrl);
        setHttpClient(httpclient);
        boolean login = login(zabbixApiUser, zabbixAipPasswd);
        if (!login) {
            logger.error("zabbix 登陆失败");
        }
    }

    private void destroy() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (Exception e) {
                logger.error("zabbix close httpclient error!", e);
            }
        }
    }

    /**
     * login zabbix
     *
     * @param user
     * @param password
     * @return
     */
    private boolean login(String user, String password) {
        if (checkAuth()) {
            return true;
        }

        ZabbixRequest request = ZabbixRequestBuilder.newBuilder().paramEntry("user", user).paramEntry("password", password)
                .method("user.login").build();
        JSONObject response = call(request);

        if (response == null || response.isEmpty()) return false;
        auth = response.getString("result");
        if (auth != null && !auth.isEmpty()) {
            logger.info("Zabbix login success!");
            this.auth = auth;
            cacheZabbixService.insertZabbixAuth(auth);
            return true;
        }
        return false;
    }

    private boolean checkAuth() {
        String auth = cacheZabbixService.getZabbixAuth();
        if (!StringUtils.isEmpty(auth)) {
            this.auth = auth;
            JSONObject filter = new JSONObject();
            filter.put("host", ZABBIX_SERVER_DEFAULT_NAME);
            ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                    .method("host.get").paramEntry("filter", filter)
                    .build();
            JSONObject getResponse = call(request);

            //ZabbixVersion version = new ZabbixVersion();
            //version.setVersion(getApiVersion());
            try {
                JSONObject result = getResponse.getJSONArray("result").getJSONObject(0);
                String hostid = result.getString("hostid");
                if (Integer.valueOf(hostid) != 0) return true;
                return false;
            } catch (Exception e) {
                logger.info("Check zabbix auth failed！");
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String getApiVersion() {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder().method("apiinfo.version").build();
        //System.err.println(request);
        JSONObject response = call(request);

//        System.err.println(request);
//        System.err.println(response);

        //System.err.println(response);
        return response.getString("result");
    }

    private JSONObject call(ZabbixRequest request) {
        if (request.getAuth() == null && !request.getMethod().equalsIgnoreCase("apiinfo.version") && !request.getMethod().equalsIgnoreCase("user.login")) {
            request.setAuth(auth);
        }
        try {
            HttpUriRequest httpRequest = org.apache.http.client.methods.RequestBuilder.post().setUri(uri)
                    .addHeader("Content-Type", "application/json")
                    .setEntity(new StringEntity(JSON.toJSONString(request), ContentType.APPLICATION_JSON)).build();
            CloseableHttpResponse response = httpClient.execute(httpRequest);
            //System.err.println(new StringEntity(JSON.toJSONString(request), ContentType.APPLICATION_JSON));
            HttpEntity entity = response.getEntity();
            byte[] data = EntityUtils.toByteArray(entity);
            JSONObject jsonObject = (JSONObject) JSON.parse(data);
            //System.err.println("Zabbix API Request : "+request);
            //System.err.println("Zabbix API Response : "+jsonObject);
            return jsonObject;
        } catch (IOException e) {
            //e.printStackTrace();
            logger.error("zabbix server 登陆失败!");
            //throw new RuntimeException("DefaultZabbixApi call exception!", e);
        }
        return new JSONObject();
    }

    private String getServerGroupName(ServerDO serverDO) {
        if (serverDO == null) return null;
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverDO.getServerGroupId());
        return serverGroupDO.getName();
    }

    /**
     * 获取主机用于zabbix监控的接口ip
     *
     * @return
     */
    private String acqServerMonitorIp(ServerDO serverDO) {
        if (serverDO == null) return null;
        ConfigPropertyDO confPropertyDO = configDao.getConfigPropertyByName(zabbix_host_monitor_public_ip);
        String value = acqServerPropertiesValue(serverDO, confPropertyDO);
        if (value == null || value.equalsIgnoreCase("false")) {
            return serverDO.getInsideIp();
        }
        return serverDO.getPublicIp();
    }

    /**
     * 主机是否被监控
     *
     * @return
     */
    @Override
    public boolean hostExists(ServerDO serverDO) {
        if (hostGet(serverDO) == 0) return false;
        return true;
    }

    /**
     * 内网ip查询hostid
     *
     * @return
     */
    private int hostGet(ServerDO serverDO) {
        if (serverDO == null) return 0;
        JSONObject filter = new JSONObject();
        filter.put("ip", acqServerMonitorIp(serverDO));
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.get").paramEntry("filter", filter)
                .build();
        JSONObject getResponse = call(request);
        return getResultId(getResponse, "hostid");
    }

    @Deprecated
    private String hostGetByName(ServerDO serverDO) {
        if (serverDO == null) return null;
        JSONObject filter = new JSONObject();
        filter.put("host", serverDO.getServerName() + "-" + serverDO.getSerialNumber());
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.get").paramEntry("filter", filter)
                .build();
        JSONObject getResponse = call(request);
        String hostid = getResponse.getJSONArray("result")
                .getJSONObject(0).getString("hostid");
        return hostid;
    }

    @Override
    public ZabbixVersion getZabbixVersion(String zabbixServerName) {
        if (StringUtils.isEmpty(zabbixServerName)) {
            zabbixServerName = ZABBIX_SERVER_DEFAULT_NAME;
        }
        JSONObject filter = new JSONObject();
        filter.put("host", zabbixServerName);
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.get").paramEntry("filter", filter)
                .build();
        JSONObject getResponse = call(request);

        ZabbixVersion version = new ZabbixVersion();
        version.setVersion(getApiVersion());
        try {
            JSONObject result = getResponse.getJSONArray("result").getJSONObject(0);
            String hostid = result.getString("hostid");
            version.setHostid(hostid);
            String name = result.getString("name");
            version.setName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 查询主机监控状态
     *
     * @param serverDO
     * @return
     */
    @Override
    public int hostGetStatus(ServerDO serverDO) {
        if (serverDO == null) return 0;
        JSONObject filter = new JSONObject();
        filter.put("ip", acqServerMonitorIp(serverDO));
        JSONObject getResponse = call(ZabbixRequestBuilder.newBuilder()
                .method("host.get").paramEntry("filter", filter)
                .build());
        return getResultId(getResponse, "status");
    }

    private boolean hostUpdateStatus(ServerDO serverDO, int status) {
        int hostid = hostGet(serverDO);
        // 主机不存在
        if (hostid == 0) return false;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.update").build();
        request.putParam("hostid", hostid);
        request.putParam("status", status);
        JSONObject getResponse = call(request);
        int hostids = getResultId(getResponse, "hostids");
        if (hostids == 0) return false;
        return true;
    }

    /**
     * zabbix监控主机名称
     *
     * @param serverDO
     * @return
     */
    private String acqZabbixServerName(ServerDO serverDO) {
        String name = serverDO.getServerName();
        String serialNumberName = "";
        if (serverDO.getSerialNumber() != null && !serverDO.getSerialNumber().isEmpty()) {
            serialNumberName = "-" + serverDO.getSerialNumber();
        }
        String envName = "";
        if (serverDO.getEnvType() != ServerDO.EnvTypeEnum.prod.getCode()) {
            envName = "-" + ServerDO.EnvTypeEnum.getEnvTypeName(serverDO.getEnvType());
        }
        return name + envName + serialNumberName;
    }

    /**
     * 普通服务器监控添加（废弃）
     *
     * @return
     */
    private boolean hostCreate(ServerDO serverDO) {
        if (hostExists(serverDO)) return true;
        if (!hostgroupCreate(serverDO)) return false;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.create").build();
        request.putParam("host", acqZabbixServerName(serverDO));
        request.putParam("interfaces", acqInterfaces(serverDO));
        request.putParam("groups", acqGroup(serverDO));
        request.putParam("templates", acqTemplate(serverDO));
        request.putParam("macros", acqMacros(serverDO));
        int proxyid = proxyGet(serverDO);
        if (proxyid != 0) {
            request.putParam("proxy_hostid", proxyid);
        }
        JSONObject getResponse = call(request);
        int hostids = getResultId(getResponse, "hostids");
        if (hostids == 0) return false;
        return true;
    }

    private boolean hostCreate(ServerDO serverDO, ZabbixHost host) {
        if (hostExists(serverDO)) return true;
        if (!hostgroupCreate(serverDO)) return false;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.create").build();
        request.putParam("host", serverDO.acqServerName());
        request.putParam("interfaces", acqInterfaces(serverDO));
        request.putParam("groups", acqGroup(serverDO));
        request.putParam("templates", acqTemplate(host.getTemplates()));

        if (host.isUseProxy()) {
            request.putParam("proxy_hostid", proxyGet(host.getProxy().getHost()));
        }
        JSONObject getResponse = call(request);
        int hostids = getResultId(getResponse, "hostids");
        if (hostids == 0) return false;
        return true;
    }

    private boolean hostDelete(ServerDO serverDO) {
        if (serverDO == null) return false;
        int hostid = hostGet(serverDO);
        if (hostid == 0) return true;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.delete").paramEntry("params", new int[]{
                        hostid
                }).build();
        JSONObject getResponse = call(request);
        int hostids = getResultId(getResponse, "hostids");
        if (hostids == 0) return false;
        return true;
    }


    /**
     * 获取当前主机的Zabbix监控模版
     *
     * @return
     */
    private JSONArray acqTemplate(ServerDO serverDO) {
        if (serverDO == null) return null;
        JSONArray templates = new JSONArray();
        ConfigPropertyDO confPropertyDO = configDao.getConfigPropertyByName("TOMCAT_APP_NAME_OPT");
        int templateid = 0;
        if (confPropertyDO != null) {
            String value = this.acqServerGroupPropertiesValue(serverDO, confPropertyDO);

            if (value == null || value.isEmpty()) {
                templateid = templateGet("Template OS Linux");
            } else {
                templateid = templateGet("Template Java");
            }
            JSONObject template = new JSONObject();
            template.put("templateid", templateid);
            templates.add(template);
            return templates;
        }
        return null;
    }

    private JSONArray acqTemplate(List<ZabbixTemplateVO> templates) {
        if (templates == null || templates.size() == 0) return new JSONArray();
        JSONArray templateArray = new JSONArray();
        for (ZabbixTemplateVO template : templates) {
            if (template.isChoose()) {
                //int  templateid = templateGet(template.getTemplateName());
                JSONObject t = new JSONObject();
                t.put("templateid", templateGet(template.getTemplateName()));
                templateArray.add(template);
            }

        }
        return templateArray;
    }

    /**
     * 获取当前主机的宏(macros)
     *
     * @return
     */
    private JSONArray acqMacros(ServerDO serverDO) {
        if (serverDO == null) return null;
        ConfigPropertyDO confPropertyDO = configDao.getConfigPropertyByName("TOMCAT_APP_NAME_OPT");
        if (confPropertyDO == null) return null;
        String appName = this.acqServerGroupPropertiesValue(serverDO, confPropertyDO);
        if (appName != null && !appName.isEmpty()) {
            JSONArray macros = new JSONArray();
            confPropertyDO = configDao.getConfigPropertyByName("TOMCAT_HTTP_PORT_OPT");
            String httpPort = this.acqServerGroupPropertiesValue(serverDO, confPropertyDO);
            JSONObject jsonAppName = new JSONObject();
            jsonAppName.put("macro", "{$APP_NAME}");
            jsonAppName.put("value", appName);
            JSONObject jsonHttpPort = new JSONObject();
            jsonHttpPort.put("macro", "{$HTTP_PORT}");
            jsonHttpPort.put("value", httpPort);
            macros.add(jsonAppName);
            macros.add(jsonHttpPort);
            return macros;
        }
        return null;
    }

    private JSONArray acqGroup(ServerDO serverDO) {
        JSONArray jsonarray = new JSONArray();
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("groupid", hostgroupGet(serverDO));
        jsonarray.add(jsonobject);
        return jsonarray;
    }


    /**
     * 获取当前主机的接口（java类有jmx）
     *
     * @return
     */
    private JSONArray acqInterfaces(ServerDO serverDO) {
        if (serverDO == null) return null;
        ConfigPropertyDO confPropertyDO = configDao.getConfigPropertyByName("TOMCAT_JMX_rmiRegistryPortPlatform_OPT");
        JSONArray interfaces = new JSONArray();
        String ip = this.acqServerMonitorIp(serverDO);
        interfaces.add(buildInterfaces(ip, 1, "10050"));
        if (confPropertyDO != null && !confPropertyDO.getProValue().isEmpty()) {
            String port = this.acqServerGroupPropertiesValue(serverDO, confPropertyDO);
            if (port != null && !port.isEmpty())
                interfaces.add(buildInterfaces(ip, 4, port));
        }
        return interfaces;
    }

    private JSONObject buildInterfaces(String ip, int type, String port) {
        JSONObject zabbixIf = new JSONObject();
        zabbixIf.put("type", type);
        zabbixIf.put("main", 1);
        zabbixIf.put("useip", 1);
        zabbixIf.put("ip", ip);
        zabbixIf.put("dns", "");
        zabbixIf.put("port", port);
        return zabbixIf;
    }


    /**
     * 按IP查询主机
     *
     * @param ip
     * @return
     */
    @Deprecated
    public int hostGetByIP(String ip) {
        JSONObject filter = new JSONObject();
        filter.put("ip", ip);
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.get").paramEntry("filter", filter)
                .build();
        JSONObject getResponse = call(request);
        return getResultId(getResponse, "hostid");
    }

    @Deprecated
    public int hostGetByName(String name) {
        JSONObject filter = new JSONObject();
        filter.put("host", name);
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.get").paramEntry("filter", filter)
                .build();
        JSONObject getResponse = call(request);
        return getResultId(getResponse, "hostid");
    }

    /**
     * 获取zabbixProxyId
     * 1:zabbix_proxy_id
     * 2:zabbix_proxy_name
     *
     * @return
     */
    private int proxyGet(ServerDO serverDO) {
        if (serverDO == null) return 0;
        ConfigPropertyDO confPropertyDO = configDao.getConfigPropertyByName(zabbix_proxy_id);
        if (confPropertyDO != null && !confPropertyDO.getProValue().isEmpty()) {
            String value = acqServerPropertiesValue(serverDO, confPropertyDO);
            if (value != null && !value.isEmpty()) return Integer.valueOf(value);
        }
        confPropertyDO = configDao.getConfigPropertyByName(zabbix_proxy_name);
        if (confPropertyDO != null && !confPropertyDO.getProValue().isEmpty()) {
            String value = acqServerPropertiesValue(serverDO, confPropertyDO);
            if (value != null && !value.isEmpty()) return proxyGet(confPropertyDO.getProValue());
        }
        if (serverDO.getServerType() == 2) {
            return proxyGet("proxy1.zabbix3.51xianqu.net");
        }
        return 0;
    }

    @Deprecated
    private int proxyGet_() {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("proxy.get")
                .build();
        JSONObject getResponse = call(request);
        return getResultId(getResponse, "proxyid");
    }

    public int proxyGet(String hostname) {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("proxy.get")
                .build();
        if (hostname != null && !hostname.isEmpty()) {
            JSONObject filter = new JSONObject();
            filter.put("host", hostname);
            request.putParam("filter", filter);
        }
        JSONObject getResponse = call(request);
        return getResultId(getResponse, "proxyid");
    }

    /**
     * 主机组是否存在
     *
     * @return
     */
    private boolean hostgroupExists(ServerDO serverDO) {
        int groupids = hostgroupGet(serverDO);
        if (groupids == 0) return false;
        return true;
    }


    private int hostgroupGet(ServerDO serverDO) {
        int groupids = hostgroupGet(getServerGroupName(serverDO));
        return groupids;
    }

    private int hostgroupGet(String name) {
        JSONObject filter = new JSONObject();
        //filter.put("name", new String[]{name});
        filter.put("name", name);
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("hostgroup.get").paramEntry("filter", filter).paramEntry("output", "extend")
                .build();
        JSONObject getResponse = call(request);
        return getResultId(getResponse, "groupid");
    }

    private boolean hostgroupCreate(ServerDO serverDO) {
        // 如果存在则法诺true
        if (hostgroupExists(serverDO)) return true;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder().method("hostgroup.create").paramEntry("name", getServerGroupName(serverDO)).build();
        JSONObject response = call(request);
        int groupids = getResultId(response, "groupids");
        if (groupids == 0) return false;
        return true;
    }

    /**
     * 创建服务器组
     *
     * @param name
     * @return
     */
    public boolean hostgroupCreate(String name) {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder().method("hostgroup.create").paramEntry("name", name).build();
        JSONObject response = call(request);
        //return response.getJSONObject("result").getJSONArray("groupids").getString(0);
        int groupids = getResultId(response, "groupids");
        if (groupids == 0) return false;
        return true;
    }

    /**
     * 查询服务器的扩展属性
     *
     * @param confPropertyDO
     * @return
     */
    private String acqServerPropertiesValue(ServerDO serverDO, ConfigPropertyDO confPropertyDO) {
        ServerGroupPropertiesDO serverGroupPropertiesDO = configDao.getServerPropertyData(serverDO.getId(), confPropertyDO.getId());
        if (serverGroupPropertiesDO == null) return null;
        return serverGroupPropertiesDO.getPropertyValue();
    }

    /**
     * 查询服务器组的扩展属性
     *
     * @param confPropertyDO
     * @return
     */
    private String acqServerGroupPropertiesValue(ServerDO serverDO, ConfigPropertyDO confPropertyDO) {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverDO.getServerGroupId());
        ServerGroupPropertiesDO serverGroupPropertiesDO = configDao.getServerGroupPropertyData(serverGroupDO.getId(), confPropertyDO.getId());
        if (serverGroupPropertiesDO == null) return null;
        return serverGroupPropertiesDO.getPropertyValue();
    }

    public int templateGet(String name) {
        JSONObject filter = new JSONObject();
        filter.put("name", name);
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("template.get").paramEntry("filter", filter)
                .build();
        JSONObject getResponse = call(request);
        return getResultId(getResponse, "templateid");
    }

    public List<ZabbixTemplateDO> templateQueryAll() {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("template.get")
                .build();
        JSONObject getResponse = call(request);
        List<ZabbixTemplateDO> list = new ArrayList<>();
        try {
            JSONArray result = getResponse.getJSONArray("result");
            if (result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                    JSONObject temp = result.getJSONObject(i);
                    list.add(new ZabbixTemplateDO(temp, 0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ZabbixProxy> proxyQueryAll() {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("proxy.get")
                .build();
        JSONObject getResponse = call(request);
        List<ZabbixProxy> list = new ArrayList<>();
        try {
            JSONArray result = getResponse.getJSONArray("result");
            if (result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                    JSONObject proxy = result.getJSONObject(i);
                    list.add(new ZabbixProxy(proxy));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询用户群组
     *
     * @param usergroup
     * @return ZabbixResponse
     */
    public int usergroupGet(String usergroup) {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("usergroup.get").build();
        request.putParam("status", 0);
        if (usergroup != null) {
            JSONObject filter = new JSONObject();
            filter.put("name", usergroup);
            request.putParam("filter", filter);
        }
        JSONObject getResponse = call(request);
        return getResultId(getResponse, "usrgrpid");
    }

    /**
     * 创建用户
     *
     * @param userDO
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> userCreate(UserDO userDO) {
        if (userDO == null)
            return new BusinessWrapper<>(ErrorCode.userNotExist.getCode(), ErrorCode.userNotExist.getMsg());
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("user.create").build();
        request.putParam("alias", userDO.getUsername());
        request.putParam("name", userDO.getDisplayName());
        request.putParam("passwd", "");
        request.putParam("usrgrps", acqUsergrps(userDO));
        JSONArray userMedias = new JSONArray();
        userMedias.add(acqUserMedias(1, userDO.getMail()));
        if (userDO.getMobile() != null && !userDO.getMobile().isEmpty()) {
            userMedias.add(acqUserMedias(3, userDO.getMobile()));
        }
        request.putParam("user_medias", userMedias);
        JSONObject getResponse = call(request);
        int userids = getResultId(getResponse, "userids");

        if (userids != 0) {
            userDO.setZabbixAuthed(UserDO.ZabbixAuthType.authed.getCode());
            userDao.updateUserZabbixAuthed(userDO);
            return new BusinessWrapper<>(true);
        }
        userDO.setZabbixAuthed(UserDO.ZabbixAuthType.noAuth.getCode());
        userDao.updateUserZabbixAuthed(userDO);
        return new BusinessWrapper<>(ErrorCode.zabbixUserCreate.getCode(), ErrorCode.zabbixUserCreate.getMsg());
    }

    private JSONArray acqUsergrps(UserDO userDO) {
        List<ServerGroupDO> list = keyboxDao.getGroupListByUsername(userDO.getUsername());
        if (list == null || list.size() == 0) return new JSONArray();
        JSONArray groups = new JSONArray();
        for (ServerGroupDO serverGroupDO : list) {
            //String usergrpName = serverGroupDO.getName().replace("group_", "users_");
            JSONObject group = new JSONObject();
            int usrgrpid = usergroupCreate(serverGroupDO);
            if (usrgrpid == 0) continue;
            group.put("usrgrpid", usergroupCreate(serverGroupDO));
            groups.add(group);
        }
        return groups;
    }

    /**
     * 添加用户组（权限为只读）
     *
     * @param serverGroupDO
     * @return
     */
    @Override
    public int usergroupCreate(ServerGroupDO serverGroupDO) {
        //需要转换名称  group_name ==> users_name
        String usergrpName = serverGroupDO.getName().replace("group_", "users_");
        int id = usergroupGet(usergrpName);
        if (id != 0) return id;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("usergroup.create").build();
        request.putParam("name", usergrpName);
        JSONObject rights = new JSONObject();
        /**
         * Possible values:
         0 - access denied;
         2 - read-only access;
         3 - read-write access.
         */
        rights.put("permission", 2);
        rights.put("id", hostgroupGet(serverGroupDO.getName()));
        request.putParam("rights", rights);
        JSONObject getResponse = call(request);
        int usrgrpids = getResultId(getResponse, "usrgrpids");
        // 自动创建告警动作
        actionCreate(serverGroupDO);
        if (usrgrpids != 0) {
            logger.info("Zabbix : usergroup " + usergrpName + " create");
            return usrgrpids;
        } else {
            logger.error("Zabbix : usergroup " + usergrpName + " create");
            return 0;
        }
    }

    @Override
    public int userGet(UserDO userDO) {
        if (userDO == null) return 0;
        JSONObject filter = new JSONObject();
        filter.put("alias", userDO.getUsername());
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("user.get").paramEntry("filter", filter)
                .build();
        JSONObject getResponse = call(request);
        return getResultId(getResponse, "userid");
    }

    /**
     * 删除用户
     *
     * @param userDO
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> userDelete(UserDO userDO) {
        if (userDO == null)
            return new BusinessWrapper<>(ErrorCode.userNotExist.getCode(), ErrorCode.userNotExist.getMsg());
        int userid = this.userGet(userDO);
        if (userid == 0) return new BusinessWrapper<>(true);
        // 数组形参数 https://www.zabbix.com/documentation/2.2/manual/api/reference/user/delete
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("user.delete").paramEntry("params", new int[]{
                        userid
                }).build();
        JSONObject getResponse = call(request);
        int userids = getResultId(getResponse, "userids");
        if (userids == 0)
            return new BusinessWrapper<>(ErrorCode.zabbixUserDelete.getCode(), ErrorCode.zabbixUserDelete.getMsg());

        userDO.setZabbixAuthed(UserDO.ZabbixAuthType.noAuth.getCode());
        userDao.updateUserZabbixAuthed(userDO);
        return new BusinessWrapper<>(true);
    }

    /**
     * 同步zabbix用户
     *
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> syncUser() {
        List<UserDO> listUserDO = userDao.getAllUser();
        for (UserDO userDO : listUserDO) {
            // 未授权的用户跳过
            if (userDO.getAuthed() == UserDO.AuthType.noAuth.getCode()) continue;
            if (userGet(userDO) != 0) {
                //更新zabbix授权状态
                userDO.setZabbixAuthed(UserDO.AuthType.authed.getCode());
                userDao.updateUserZabbixAuthed(userDO);
                continue;
            }
            userCreate(userDO);
            logger.info("Zabbix : add user " + userDO.getUsername());
        }
        cleanZabbixUser();
        return new BusinessWrapper<>(true);
    }

    /**
     * 清理zabbix中的账户
     */
    public void cleanZabbixUser() {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("user.get")
                .build();
        JSONObject getResponse = call(request);
        JSONArray result = getResponse.getJSONArray("result");
        for (int i = 0; i < result.size(); i++) {
            JSONObject user = result.getJSONObject(i);

            UserDO userDO = userDao.getUserByName(user.get("alias").toString());
            if (userDO != null) continue;
            for (String userName : excludeUsers) {
                if (user.get("alias").equals(userName)) {
                    userDO = null;
                    break;
                }
            }
            if (userDO != null) {
                logger.info("Zabbix : del user " + userDO.getUsername());
                this.userDelete(userDO);
            }
        }
    }

    /**
     * 校验账户
     *
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> checkUser() {
        List<UserDO> listUserDO = userDao.getAllUser();
        for (UserDO userDO : listUserDO) {
            // 未授权的用户跳过
            if (userDO.getZabbixAuthed() == UserDO.ZabbixAuthType.noAuth.getCode()) continue;
            userUpdate(userDO);
            logger.info("Zabbix : update user " + userDO.getUsername());
        }
        return new BusinessWrapper<>(true);
    }

    /**
     * 更新用户
     *
     * @param userDO
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> userUpdate(UserDO userDO) {
        if (userDO == null)
            return new BusinessWrapper<>(ErrorCode.userNotExist.getCode(), ErrorCode.userNotExist.getMsg());
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("user.update").build();
        request.putParam("userid", userGet(userDO));
        request.putParam("name", userDO.getDisplayName());
        request.putParam("usrgrps", acqUsergrps(userDO));
        JSONArray userMedias = new JSONArray();
        userMedias.add(acqUserMedias(1, userDO.getMail()));
        if (userDO.getMobile() != null && !userDO.getMobile().isEmpty()) {
            userMedias.add(acqUserMedias(3, userDO.getMobile()));
        }
        request.putParam("user_medias", userMedias);
        JSONObject getResponse = call(request);
        int userids = getResultId(getResponse, "userids");
        if (userids != 0) {
            userDO.setZabbixAuthed(UserDO.ZabbixAuthType.authed.getCode());
            userDao.updateUserZabbixAuthed(userDO);
            return new BusinessWrapper<>(true);
        }
        userDO.setZabbixAuthed(UserDO.ZabbixAuthType.noAuth.getCode());
        userDao.updateUserZabbixAuthed(userDO);
        return new BusinessWrapper<>(ErrorCode.zabbixUserCreate.getCode(), ErrorCode.zabbixUserCreate.getMsg());
    }

    /**
     * 查询主机所有的监控项
     *
     * @param
     * @return
     */
    @Deprecated
    public JSONObject itemGetAll(ServerDO serverDO) {
        if (serverDO == null) return null;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("item.get").build();
        request.putParam("output", "extend");
        request.putParam("hostids", hostGet(serverDO));
        request.putParam("sortfield", "name");
        return call(request);
    }


    /**
     * 按名称查询主机的监控项
     *
     * @param itemName
     * @param itemKey
     */
    public int itemGet(ServerDO serverDO, String itemName, String itemKey) {
        if (serverDO == null) return 0;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("item.get").build();
        request.putParam("output", "extend");
        request.putParam("hostids", hostGet(serverDO));
        if (itemName != null) {
            JSONObject name = new JSONObject();
            name.put("name", itemName);
            request.putParam("search", name);
        }
        if (itemKey != null) {
            JSONObject key = new JSONObject();
            key.put("key_", itemKey);
            request.putParam("search", key);
        }
        request.putParam("sortfield", "name");
        JSONObject getResponse = call(request);
        return getResultId(getResponse, "itemid");
    }

    /**
     * 查询告警动作
     */
    public int actionGet(ServerGroupDO serverGroupDO) {
        if (serverGroupDO == null) return 0;
        //需要转换名称  group_name ==> users_name
        String usergrpName = serverGroupDO.getName().replace("group_", "users_");
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("action.get").build();
        request.putParam("output", "extend");
        request.putParam("selectOperations", "extend");

        JSONObject filter = new JSONObject();
        filter.put("name", "Report problems to " + usergrpName);
        request.putParam("filter", filter);

        JSONObject getResponse = call(request);

        return getResultId(getResponse, "actionid");

    }

    @Override
    public int actionCreate(ServerGroupDO serverGroupDO) {
        int actionid = actionGet(serverGroupDO);
        if (actionid != 0) return actionid;
        //需要转换名称  group_name ==> users_name
        String usergrpName = serverGroupDO.getName().replace("group_", "users_");

        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("action.create").build();
        request.putParam("name", "Report problems to " + usergrpName);
        request.putParam("eventsource", 0);
        request.putParam("status", 0);
        //默认操作步骤持续时间。必须大于60秒。
        request.putParam("esc_period", 3600);
        request.putParam("def_shortdata", "{TRIGGER.NAME}: {TRIGGER.STATUS}");
        request.putParam("def_longdata", "ServerName:{HOST.NAME} IP:{HOST.IP}\r\nTrigger: {TRIGGER.NAME}\r\nTrigger severity: {TRIGGER.SEVERITY}\r\nItem values: {ITEM.NAME1} ({HOST.NAME1}:{ITEM.KEY1}): {ITEM.VALUE1}\r\nOriginal event ID: {EVENT.ID}");
        // operations 操作
        JSONArray operations = new JSONArray();
        JSONObject operation = new JSONObject();
        operation.put("operationtype", 0);
        operation.put("esc_period", 0);
        operation.put("esc_step_from", 1);
        operation.put("esc_step_to", 1);
        operation.put("evaltype", 0);

        JSONArray opmessage_grp = new JSONArray();
        JSONObject usrgrp = new JSONObject();
        usrgrp.put("usrgrpid", usergroupGet(usergrpName));
        opmessage_grp.add(usrgrp);
        operation.put("opmessage_grp", opmessage_grp);

        JSONObject opmessage = new JSONObject();
        opmessage.put("mediatypeid", 0);
        opmessage.put("default_msg", 1);
        opmessage.put("subject", "{TRIGGER.STATUS}: {TRIGGER.NAME}");
        opmessage.put("message", "Trigger: {TRIGGER.NAME}\r\nTrigger status: {TRIGGER.STATUS}\r\nTrigger severity: {TRIGGER.SEVERITY}\r\nTrigger URL: {TRIGGER.URL}\r\n\r\nItem values:\r\n\r\n1. {ITEM.NAME1} ({HOST.NAME1}:{ITEM.KEY1}): {ITEM.VALUE1}\r\n2. {ITEM.NAME2} ({HOST.NAME2}:{ITEM.KEY2}): {ITEM.VALUE2}\r\n3. {ITEM.NAME3} ({HOST.NAME3}:{ITEM.KEY3}): {ITEM.VALUE3}\r\n\r\nOriginal event ID: {EVENT.ID}");
        operation.put("opmessage", opmessage);
        operations.add(operation);

        request.putParam("operations", operations);

        JSONObject filter = new JSONObject();
        filter.put("evaltype", 1);
        //filter.put("formula", "A and B");
        JSONArray conditions = new JSONArray();
        JSONObject conditionA = new JSONObject();
        /*
        Possible values for trigger actions:
        0 - host group;
        1 - host;
        2 - trigger;
        3 - trigger name;
        4 - trigger severity;
        5 - trigger value;
        6 - time period;
        13 - host template;
        15 - application;
        16 - maintenance status.
         */
        conditionA.put("conditiontype", 16);
        /**
         * Possible values:
         0 - (default) =;
         1 - <>;
         2 - like;
         3 - not like;
         4 - in;
         5 - >=;
         6 - <=;
         7 - not in.
         */
        conditionA.put("operator", 7);
        conditionA.put("value", "");
        conditionA.put("formulaid", "A");
        conditions.add(conditionA);

        JSONObject conditionB = new JSONObject();
        conditionB.put("conditiontype", 5);
        //  0 - (default) =;
        conditionB.put("operator", 0);
        /**
         *   https://www.zabbix.com/documentation/3.0/manual/api/reference/trigger/object#trigger
         *   Possible values are:
         0 - (default) OK;
         1 - problem.
         */
        conditionB.put("value", "1");
        conditionB.put("formulaid", "B");
        conditions.add(conditionB);

        JSONObject conditionC = new JSONObject();
        // 0 - host group;
        conditionC.put("conditiontype", 0);
        // 1 - <>;
        conditionC.put("operator", 1);
        conditionC.put("value", hostgroupGet("group_gray"));
        conditionC.put("formulaid", "C");
        conditions.add(conditionC);

        JSONObject conditionD = new JSONObject();
        // 0 - host group;
        conditionD.put("conditiontype", 0);
        // 0 - (default) =;
        conditionD.put("operator", 0);
        conditionD.put("value", hostgroupGet(serverGroupDO.getName()));
        conditionD.put("formulaid", "D");
        conditions.add(conditionD);

        filter.put("conditions", conditions);
        request.putParam("filter", filter);
        JSONObject getResponse = call(request);

        return getResultId(getResponse, "actionids");

    }


    /**
     * 查询主机监控项历史数据
     *
     * @param itemName
     * @param itemKey
     * @param historyType
     * @param limit
     * @return
     */
    @Override
    public JSONObject historyGet(ServerDO serverDO, String itemName, String itemKey, int historyType, int limit) {
        if (serverDO == null) return null;
        int itemid = itemGet(serverDO, itemName, itemKey);
        if (itemid == 0) return null;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("history.get").build();
        request.putParam("history", historyType);
        request.putParam("itemids", itemid);
        request.putParam("sortfield", "clock");
        request.putParam("sortorder", "DESC");
        request.putParam("limit", limit);
        return call(request);
    }

    @Override
    public JSONObject historyGet(ServerDO serverDO, String itemName, String itemKey, int historyType, String timestampFrom, String timestampTill) {
        if (serverDO == null) return null;
        int itemid = itemGet(serverDO, itemName, itemKey);
        if (itemid == 0) return null;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("history.get").build();
        request.putParam("history", historyType);
        request.putParam("itemids", itemid);
        request.putParam("sortfield", "clock");
        request.putParam("sortorder", "DESC");
        request.putParam("time_from", timestampFrom);
        request.putParam("time_till", timestampTill);
        return call(request);
    }


    /**
     * 发布接口调用：关闭服务器组的zabbix监控
     *
     * @param statusType 0开启  1关闭
     * @param key        调用密钥
     * @param project    项目名（同主机名称）
     * @param group      组名
     * @param env        环境
     */
    public void api(int statusType, String key, String project, String group, String env) {
        if (!apiCheckKey(key)) return;
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_" + project);
        if (serverGroupDO == null) return;
        List<ServerDO> servers = serverDao.acqServersByGroupId(serverGroupDO.getId());
        if (servers == null || servers.get(0) == null) return;
        for (ServerDO serverDO : servers) {
            if (!ServerDO.EnvTypeEnum.getEnvTypeName(serverDO.getEnvType()).equals(env)) continue;
            this.hostUpdateStatus(serverDO, statusType);
        }
    }


    private JSONObject acqUserMedias(int mediatypeid, String sendto) {
        if (sendto == null) return null;
        JSONObject medias = new JSONObject();
        medias.put("mediatypeid", mediatypeid);
        medias.put("sendto", sendto);
        medias.put("active", 0);
        medias.put("severity", 48);
        medias.put("period", "1-7,00:00-24:00");
        return medias;
    }

    private int getResultId(JSONObject response, String key) {
        if (response == null) return 0;
        try {
            JSONArray result = response.getJSONArray("result");
            if (result == null || key == null) return 0;
            return getResultIdByArray(result, key);
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                logger.error("Zabbix返回值转换");
            }
            JSONObject result = response.getJSONObject("result");
            if (result == null || key == null) return 0;
            return getResultIdByObject(result, key);
        }
    }

    private int getResultIdByArray(JSONArray result, String key) {
        if (result == null || key == null) return 0;
        try {
            return Integer.parseInt(result.getJSONObject(0).getString(key));
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                logger.error("zabbix 返回值错误!");
            }
        }
        //{"result":{"groupids":["135"]},"id":3,"jsonrpc":"2.0"}
        try {
            List<Integer> arrayList = JSON.parseArray(result.getJSONObject(0).getString(key), Integer.class);
            return arrayList.get(0);
        } catch (Exception e) {
            // 取值错误
        }
        return 0;
    }

    private int getResultIdByObject(JSONObject result, String key) {
        try {
            return Integer.parseInt(result.getString(key));
        } catch (Exception e) {
            //logger.error("zabbix 返回值错误!", e);
        }
        //{"result":{"groupids":["135"]},"id":3,"jsonrpc":"2.0"}
        try {
            List<Integer> arrayList = JSON.parseArray(result.getString(key), Integer.class);
            return arrayList.get(0);
        } catch (Exception e) {
            // 取值错误
        }
        return 0;
    }

    @Override
    public BusinessWrapper<Boolean> refresh() {

        schedulerManager.registerJob(() -> {
            List<ServerDO> servers = new ArrayList<>();
            servers.addAll(serverDao.queryServerByServerType(ServerDO.ServerTypeEnum.ecs.getCode()));
            servers.addAll(serverDao.queryServerByServerType(ServerDO.ServerTypeEnum.vm.getCode()));
            for (ServerDO serverDO : servers) {
                if (hostExists(serverDO)) {
                    serverDO.setZabbixMonitor(this.isMonitor);
                    serverDO.setZabbixStatus(hostGetStatus(serverDO));
                    // 新增更新tomcatVersion数据
                    updateTomcatVersionByServer(serverDO);
                } else {
                    serverDO.setZabbixMonitor(this.noMonitor);
                    serverDO.setZabbixStatus(-1);
                }
                serverDao.updateServerGroupServer(serverDO);
            }
        });

        return new BusinessWrapper<>(true);
    }

    private void updateTomcatVersionByServer(ServerDO serverDO) {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverDO.getServerGroupId());
        if (configServerGroupService.isTomcatServer(serverGroupDO)) {
            String version = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryTomcatVersion(serverDO, 1));
            if (StringUtils.isEmpty(serverDO.getExtTomcatVersion()) || !serverDO.getExtTomcatVersion().equals(version)) {
                serverDO.setExtTomcatVersion(version);
                serverDao.updateServerTomcatVersion(serverDO);
            }
        }
    }


    @Override
    public BusinessWrapper<Boolean> addMonitor(long serverId) {
        ServerDO serverDO = serverDao.getServerInfoById(serverId);
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverDO.getServerGroupId());
        if (hostgroupGet(serverDO) == 0) {
            hostgroupCreate(serverGroupDO.getName());
        }
        if (hostCreate(serverDO)) {
            serverDO.setZabbixMonitor(isMonitor);
            serverDao.updateServerGroupServer(serverDO);
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.zabbixHostCreate.getCode(), ErrorCode.zabbixHostCreate.getMsg());
        }
    }

    @Override
    public BusinessWrapper<Boolean> delMonitor(long serverId) {
        ServerDO serverDO = serverDao.getServerInfoById(serverId);
        if (hostDelete(serverDO)) {
            serverDO.setZabbixMonitor(noMonitor);
            serverDao.updateServerGroupServer(serverDO);
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.zabbixHostDelete.getCode(), ErrorCode.zabbixHostDelete.getMsg());
        }
    }

    @Override
    public BusinessWrapper<Boolean> repair(long serverId) {
        try {
            ServerDO serverDO = serverDao.getServerInfoById(serverId);
            // 检查服务器监控配置
            if (hostExists(serverDO)) {
                serverDO.setZabbixMonitor(1);
                serverDO.setZabbixStatus(hostGetStatus(serverDO));
            } else {
                serverDO.setZabbixMonitor(0);
                // hostGetStatus(serverDO);
                serverDO.setZabbixMonitor(1);
            }
            serverDao.updateServerGroupServer(serverDO);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<>(false);
        }
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> disableMonitor(long serverId) {
        ServerDO serverDO = serverDao.getServerInfoById(serverId);
        if (hostUpdateStatus(serverDO, hostStatusDisable)) {
            serverDO.setZabbixMonitor(hostStatusDisable);
            serverDao.updateServerGroupServer(serverDO);
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.zabbixHostDisable.getCode(), ErrorCode.zabbixHostDisable.getMsg());
        }
    }

    @Override
    public BusinessWrapper<Boolean> enableMonitor(long serverId) {
        ServerDO serverDO = serverDao.getServerInfoById(serverId);
        if (hostUpdateStatus(serverDO, hostStatusEnable)) {
            serverDO.setZabbixMonitor(hostStatusEnable);
            serverDao.updateServerGroupServer(serverDO);
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.zabbixHostEisable.getCode(), ErrorCode.zabbixHostEisable.getMsg());
        }
    }

    /**
     * 持续集成调用接口
     *
     * @param key
     * @param type
     * @param project
     * @param group
     * @param env
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> ci(String key, int type, String project, String group, String env, Long deployId, String bambooDeployVersion,
                                       int bambooBuildNumber, String bambooDeployProject, boolean bambooDeployRollback,
                                       String bambooManualBuildTriggerReasonUserName, int errorCode, String branchName, int deployType) {
        /*
          https://cmdb.51xianqu.net/api/zabbix/monitor/type=1&key=xxxxxxx&project=cmdb&group=cmdb-production&env=production
        */
        if (!apiCheckKey(key))
            return new BusinessWrapper<>(ErrorCode.zabbixCIKeyCheckFailure.getCode(), ErrorCode.zabbixCIKeyCheckFailure.getMsg());

        CiDeployStatisticsDO ciDeployStatisticsDO = new CiDeployStatisticsDO(project, group, env, type, deployId, bambooDeployVersion,
                bambooBuildNumber, bambooDeployProject, bambooDeployRollback,
                bambooManualBuildTriggerReasonUserName, errorCode, branchName, deployType);
        ciService.doCiDeployStatistics(ciDeployStatisticsDO);
        // 只有war类部署才关闭监控
        if (deployType != CiDeployStatisticsDO.DeployTypeEnum.war.getCode()) return new BusinessWrapper<>(true);
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_" + project);
        if (serverGroupDO == null)
            return new BusinessWrapper<>(ErrorCode.zabbixCIServerGroupNotExist);
        List<ServerDO> listServerDO = serverDao.acqServersByGroupId(serverGroupDO.getId());
        if (listServerDO == null || listServerDO.size() == 0)
            return new BusinessWrapper<>(ErrorCode.serverGroupServerNull);
        for (ServerDO serverDO : listServerDO) {
            String serverEnv = ServerDO.EnvTypeEnum.getEnvTypeName(serverDO.getEnvType());
            if (!serverEnv.equals(env)) continue;
            if (hostUpdateStatus(serverDO, type)) {
                logger.info("Zabbix : ci update " + serverDO.getServerName() + "/" + serverDO.getInsideIp() + "/" + serverEnv + " status " + type);
            } else {
                logger.error("Zabbix : ci update " + serverDO.getServerName() + "/" + serverDO.getInsideIp() + "/" + serverEnv + " status " + type);
            }
        }
        return new BusinessWrapper<>(true);
    }

    /**
     * 检查key
     *
     * @param key
     * @return
     */
    private boolean apiCheckKey(String key) {
        HashMap<String, String> configMap = acqConifMap();
        String zabbixApiKey = configMap.get(ZabbixItemEnum.ZABBIX_API_KEY.getItemKey());
        if (key == null) {
            logger.error("cmdb.zabbix.api.key为空");
            return false;
        }
        if (!key.equals(zabbixApiKey)) {
            logger.error("cmdb.zabbix.api.key不匹配");
            return false;
        }
        return true;
    }


    /**
     * 判断用户是否在用户组中（服务器组和用户组会转换名称  group_test --> users_test)
     *
     * @param userDO
     * @param serverGroupDO
     * @return
     */
    public int checkUserInUsergroup(UserDO userDO, ServerGroupDO serverGroupDO) {
        int userid = this.userGet(userDO);
        if (userid == 0) return 2;
        String usergrpName = serverGroupDO.getName().replace("group_", "users_");
        int usrgrpid = usergroupGet(usergrpName);
        if (usrgrpid == 0) return 3;
        // 数组形参数
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("user.get").paramEntry("userids", new int[]{
                        userid
                }).paramEntry("usrgrpids", new int[]{
                        usrgrpid
                })
                .build();
        JSONObject getResponse = call(request);
        int resultUserid = getResultId(getResponse, "userid");
        if (resultUserid == 0)
            return 0;
        return 1;
    }

    @Override
    public TableVO<List<ZabbixTemplateVO>> getTemplatePage(String templateName, int enabled, int page, int length) {
        long size = zabbixDao.getTemplateSize(templateName, enabled);
        List<ZabbixTemplateDO> list = zabbixDao.getTemplatePage(templateName, enabled, page * length, length);
        List<ZabbixTemplateVO> voList = new ArrayList<>();
        for (ZabbixTemplateDO template : list)
            voList.add(new ZabbixTemplateVO(template));
        return new TableVO<>(size, voList);
    }


    private List<ZabbixTemplateVO> getTemplates(long serverGroupId) {
        TableVO<List<ZabbixTemplateVO>> tableVO = getTemplatePage("", 1, 0, 20);
        List<ZabbixTemplateVO> voList = tableVO.getData();
        ServerGroupDO serverGroupDO = new ServerGroupDO(serverGroupId);
        String[] templates = configServerGroupService.queryZabbixTemplates(serverGroupDO);
        for (String templateName : templates) {
            for (ZabbixTemplateVO zabbixTemplateVO : voList) {
                if (zabbixTemplateVO.getTemplateName().equalsIgnoreCase(templateName)) {
                    zabbixTemplateVO.setChoose(true);
                    break;
                }
            }
        }
        return voList;
    }


    @Override
    public ZabbixHost getHost(long serverId) {
        ServerDO serverDO = serverDao.getServerInfoById(serverId);
        if (serverDO == null) return new ZabbixHost();

        List<ZabbixTemplateVO> templates = getTemplates(serverDO.getServerGroupId());
        ZabbixHost host = new ZabbixHost(templates);
        invokeProxy(serverDO.getServerGroupId(), host);
        host.setHost(serverDO.acqServerName());
        host.setIp(acqServerMonitorIp(serverDO));
        return host;
    }

    @Override
    public BusinessWrapper<Boolean> saveHost(ZabbixHost host) {
        ServerDO serverDO = serverDao.getServerInfoById(host.getServerVO().getId());
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverDO.getServerGroupId());
        // 创建Zabbix服务器组
        if (hostgroupGet(serverDO) == 0) {
            hostgroupCreate(serverGroupDO.getName());
        }
        // 添加服务器监控
        if (hostCreate(serverDO, host)) {
            serverDO.setZabbixMonitor(isMonitor);
            serverDao.updateServerGroupServer(serverDO);
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.zabbixHostCreate.getCode(), ErrorCode.zabbixHostCreate.getMsg());
        }
    }


    @Override
    public List<ZabbixProxy> queryProxy() {
        return proxyQueryAll();
    }


    private void invokeProxy(long serverGroupId, ZabbixHost host) {
        List<ZabbixProxy> proxys = proxyQueryAll();

        ServerGroupDO serverGroupDO = new ServerGroupDO(serverGroupId);
        String proxyName = configServerGroupService.queryZabbixProxy(serverGroupDO);
        for (ZabbixProxy proxy : proxys) {
            if (proxy.getHost().equalsIgnoreCase(proxyName)) {
                proxy.setSelected(true);
                host.setUseProxy(true);
                host.setProxy(proxy);
                break;
            }
        }

        host.setProxys(proxys);
    }

    @Override
    public BusinessWrapper<Boolean> setTemplate(long id) {
        try {
            ZabbixTemplateDO zabbixTemplateDO = zabbixDao.getTemplate(id);
            if (zabbixTemplateDO.getEnabled() == 0) {
                zabbixTemplateDO.setEnabled(1);
            } else {
                zabbixTemplateDO.setEnabled(0);
            }
            zabbixDao.updateTemplate(zabbixTemplateDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delTemplate(long id) {
        try {
            zabbixDao.delTemplate(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }


    @Override
    public BusinessWrapper<Boolean> rsyncTemplate() {
        List<ZabbixTemplateDO> list = templateQueryAll();
        for (ZabbixTemplateDO zabbixTemplateDO : list) {
            ZabbixTemplateDO template = zabbixDao.getTemplateByName(zabbixTemplateDO.getTemplateName());
            if (template == null) {
                try {
                    zabbixDao.addTemplate(zabbixTemplateDO);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new BusinessWrapper<Boolean>(false);
                }
            }
        }
        return new BusinessWrapper<Boolean>(true);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
