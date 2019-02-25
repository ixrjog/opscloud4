package com.sdg.cmdb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sdg.cmdb.dao.cmdb.*;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.zabbix.*;
import com.sdg.cmdb.domain.zabbix.response.*;
import com.sdg.cmdb.plugin.cache.CacheZabbixService;
import com.sdg.cmdb.service.ConfigServerGroupService;
import com.sdg.cmdb.service.ZabbixServerService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class ZabbixServerServiceImpl implements ZabbixServerService, InitializingBean {

    public static final String ZABBIX_API = "4.0";

    // TODO 用于检测登录是否正常
    public static final String ZABBIX_SERVER_DEFAULT_NAME = "Zabbix server";

    public static final String ZABBIX_KEY_RESULT = "result";

    public static final String ZABBIX_KEY_APIINFO = "apiinfo.version";

    private String defaultUsergroupId;


    @Value("#{cmdb['zabbix.url']}")
    private String zabbixUrl;
    @Value("#{cmdb['zabbix.user']}")
    private String zabbixUser;
    @Value("#{cmdb['zabbix.passwd']}")
    private String zabbixPasswd;

    private static final Logger logger = LoggerFactory.getLogger(ZabbixServerServiceImpl.class);

    @Resource
    private CacheZabbixService cacheZabbixService;

    @Autowired
    private ConfigServerGroupService configServerGroupService;

    @Autowired
    private ServerGroupDao serverGroupDao;

    @Autowired
    private ServerDao serverDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private KeyboxDao keyboxDao;

    @Autowired
    private ZabbixServerDao zabbixDao;

    private URI uri;
    // TODO token
    private volatile String auth;

    private CloseableHttpClient httpClient;


    @Override
    public ZabbixResponseHost getHost(ServerDO serverDO) {
        if (serverDO == null) return new ZabbixResponseHost();
        JSONObject filter = new JSONObject();
        filter.put("ip", configServerGroupService.queryZabbixMonitorIp(serverDO));
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.get").paramEntry("filter", filter)
                .build();
        try {
            JSONObject response = call(request);
            Gson gson = new GsonBuilder().create();
            List<ZabbixResponseHost> hostList = gson.fromJson(response.getJSONArray(ZABBIX_KEY_RESULT).toString(), new TypeToken<ArrayList<ZabbixResponseHost>>() {
            }.getType());
            for (ZabbixResponseHost host : hostList)
                return host;
        } catch (Exception e) {
            logger.info("Zabbix查询主机失败！");
        }
        return new ZabbixResponseHost();
    }

    @Override
    public void updateHostTemplates(ServerDO serverDO) {
        // ZabbixHost使用的模版
        List<ZabbixResponseTemplate> hostTemplates = getHostTemplates(serverDO);
        // id / name
        HashMap<String, String> templateMap = new HashMap<>();
        for (ZabbixResponseTemplate zabbixResponseTemplate : hostTemplates)
            templateMap.put(zabbixResponseTemplate.getTemplateid(), zabbixResponseTemplate.getName());
        boolean isUpdate = false;

        // 服务器组模版配置
        List<ZabbixTemplateVO> groupTemplates = getTemplates(serverDO.getServerGroupId());
        for (ZabbixTemplateVO zabbixTemplateVO : groupTemplates) {
            if (!zabbixTemplateVO.isChoose()) continue;
            String templateid = zabbixTemplateVO.getTemplateid();
            if (!templateMap.containsKey(templateid)) {
                isUpdate = true;
                templateMap.put(templateid, zabbixTemplateVO.getTemplateName());
            }
        }
        if (isUpdate)
            updateHostTemplates(serverDO, templateMap);
    }

    private void updateHostTemplates(ServerDO serverDO, HashMap<String, String> templateMap) {
        ZabbixResponseHost host = getHost(serverDO);
        // 主机不存在
        if (host == null) return;
        JSONArray templates = new JSONArray();
        for (String key : templateMap.keySet())
            templates.add(key);
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.update").build();
        request.putParam("hostid", host.getHostid());
        request.putParam("templates", templates);
        try {
            JSONObject response = call(request);
        } catch (Exception e) {

        }
    }

    @Override
    public void updateHostMacros(ServerDO serverDO) {
        try {
            String macrosOpt = configServerGroupService.queryZabbixMacros(serverDO);
            if (StringUtils.isEmpty(macrosOpt))
                return;
            JSONArray macros = JSONArray.parseArray(macrosOpt);
            if (macros.size() == 0) return;

            Gson gson = new GsonBuilder().create();
            List<ZabbixResponseMacro> macroList = gson.fromJson(macros.toString(),
                    new TypeToken<ArrayList<ZabbixResponseMacro>>() {
                    }.getType());
            HashMap<String, String> macrosMap = new HashMap<>();
            // TODO 服务器组宏配置
            for (ZabbixResponseMacro macro : macroList)
                macrosMap.put(macro.getMacro(), macro.getValue());
            boolean isUpdate = false;
            // TODO 当前Host的宏
            List<ZabbixResponseMacro> list = getHostMacros(serverDO);
            if(list.size() == 0)
                isUpdate = true;
            for (ZabbixResponseMacro macro : list) {
                if (!macrosMap.containsKey(macro.getMacro())) {
                    isUpdate = true;
                    macrosMap.put(macro.getMacro(), macro.getValue());
                }else{
                   if(!macro.getValue().equals(macrosMap.get(macro.getMacro()))) {
                       isUpdate = true;
                       macrosMap.put(macro.getMacro(), macro.getValue());
                   }
                }
            }
            if(isUpdate)
                updateHostMacros(serverDO,macrosMap);
        } catch (Exception e) {
        }

    }

    public void updateHostMacros(ServerDO serverDO,HashMap<String, String> macrosMap) {
        JSONArray macros = new JSONArray();
        for(String key:macrosMap.keySet()){
            JSONObject macro = new JSONObject();
            macro.put("macro",key);
            macro.put("value",macrosMap.get(key));
            macros.add(macro);
        }

        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.update").build();
        request.putParam("hostid", getHost(serverDO.getId()).getHostid());
        request.putParam("macros", macros);
        try {
            JSONObject response = call(request);
        } catch (Exception e) {
            logger.info("Zabbix更新主机宏失败！");
        }
    }

    /**
     * 查询 Host Macros
     *
     * @param serverDO
     * @return
     */
    private List<ZabbixResponseMacro> getHostMacros(ServerDO serverDO) {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.get").build();
        request.putParam("output", new String[]{"hostid"});
        request.putParam("selectMacros", new String[]{"macro", "value"});
        ZabbixResponseHost host = getHost(serverDO);
        request.putParam("hostids", host.getHostid());
        try {
            JSONObject response = call(request);
            //  String reslut = response.getJSONArray(ZABBIX_KEY_RESULT).toJSONString();
            String macros = response.getJSONArray(ZABBIX_KEY_RESULT).getJSONObject(0).getJSONArray("macros").toJSONString();
            Gson gson = new GsonBuilder().create();
            List<ZabbixResponseMacro> list = gson.fromJson(macros,
                    new TypeToken<ArrayList<ZabbixResponseMacro>>() {
                    }.getType());
            return list;
        } catch (Exception e) {
            logger.info("Zabbix查询主机宏失败！");
        }
        return new ArrayList<ZabbixResponseMacro>();
    }


    @Override
    public ZabbixResponseHost getHost(long serverId) {
        return getHost(serverDao.getServerInfoById(serverId));
    }

    @Override
    public ZabbixHost getZabbixHost(long serverId) {
        ServerDO serverDO = serverDao.getServerInfoById(serverId);
        if (serverDO == null) return new ZabbixHost();

        List<ZabbixTemplateVO> templates = getTemplates(serverDO.getServerGroupId());
        ZabbixHost host = new ZabbixHost(templates);
        invokeProxy(serverDO.getServerGroupId(), host);
        host.setHost(serverDO.acqServerName());

        host.setIp(configServerGroupService.queryZabbixMonitorIp(serverDO));
        return host;
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


    private void invokeProxy(long serverGroupId, ZabbixHost host) {
        List<ZabbixProxy> proxys = proxyQueryAll();
        ServerGroupDO serverGroupDO = new ServerGroupDO(serverGroupId);
        String proxyName = configServerGroupService.queryZabbixProxy(serverGroupDO);
        for (ZabbixProxy proxy : proxys) {
            if (proxy.getHost().equalsIgnoreCase(proxyName)) {
                proxy.setSelected(true);
                host.setUseProxy(true);
                // host.setProxy(proxy);
                break;
            }
        }

        host.setProxys(proxys);
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


    @Override
    public List<ZabbixResponseTemplate> getHostTemplates(ServerDO serverDO) {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.get").build();
        request.putParam("output", new String[]{"hostid"});
        request.putParam("selectParentTemplates", new String[]{"templateid", "name"});
        ZabbixResponseHost host = getHost(serverDO);
        request.putParam("hostids", host.getHostid());
        try {
            JSONObject response = call(request);
            String reslut = response.getJSONArray(ZABBIX_KEY_RESULT).toJSONString();
            Gson gson = new GsonBuilder().create();
            List<ZabbixResponseParentTemplate> list = gson.fromJson(reslut,
                    new TypeToken<ArrayList<ZabbixResponseParentTemplate>>() {
                    }.getType());
            return list.get(0).getParentTemplates();
        } catch (Exception e) {
            logger.info("Zabbix查询主机模版失败！");
        }
        return new ArrayList<ZabbixResponseTemplate>();
    }

    @Override
    public ZabbixResponseTemplate getTemplate(String name) {
        JSONObject filter = new JSONObject();
        filter.put("name", name);
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("template.get").paramEntry("filter", filter)
                .build();
        JSONObject response = call(request);
        String reslut = response.getJSONArray(ZABBIX_KEY_RESULT).toJSONString();
        Gson gson = new GsonBuilder().create();
        List<ZabbixResponseTemplate> templateList = gson.fromJson(reslut,
                new TypeToken<ArrayList<ZabbixResponseTemplate>>() {
                }.getType());
        return templateList.get(0);
    }

    @Override
    public List<ZabbixResponseTemplate> queryTemplates() {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("template.get")
                .build();
        JSONObject response = call(request);
        String reslut = response.getJSONArray(ZABBIX_KEY_RESULT).toJSONString();
        Gson gson = new GsonBuilder().create();
        List<ZabbixResponseTemplate> templateList = gson.fromJson(reslut,
                new TypeToken<ArrayList<ZabbixResponseTemplate>>() {
                }.getType());
        return templateList;
    }

    @Override
    public ZabbixResponseUsergroup getUsergroup(String usergroup) {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("usergroup.get").build();
        request.putParam("status", 0);
        if (usergroup != null) {
            JSONObject filter = new JSONObject();
            filter.put("name", usergroup);
            request.putParam("filter", filter);
        }
        JSONObject response = call(request);
        String reslut = response.getJSONArray(ZABBIX_KEY_RESULT).toJSONString();
        try {
            Gson gson = new GsonBuilder().create();
            List<ZabbixResponseUsergroup> usergroupList = gson.fromJson(reslut,
                    new TypeToken<ArrayList<ZabbixResponseUsergroup>>() {
                    }.getType());
            //System.err.println(response);
            return usergroupList.get(0);
        } catch (Exception e) {
        }
        return new ZabbixResponseUsergroup();
    }


    @Override
    public String createUsergroup(ServerGroupDO serverGroupDO) {
        // TODO 创建用户组
        String usergrpName = serverGroupDO.getName().replace("group_", "users_");
        ZabbixResponseUsergroup usergroup = getUsergroup(usergrpName);
        if (usergroup != null && !StringUtils.isEmpty(usergroup.getUsrgrpid()))
            return usergroup.getUsrgrpid();
        // TODO 创建主机组
        String groupid = createHostgroup(serverGroupDO.getName());

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
        rights.put("id", groupid);
        request.putParam("rights", rights);
        JSONObject response = call(request);
        try {
            String reslut = response.getJSONObject(ZABBIX_KEY_RESULT).getJSONArray("usrgrpids").toJSONString();
            Gson gson = new GsonBuilder().create();
            List<String> usrgrpids = gson.fromJson(reslut,
                    new TypeToken<ArrayList<String>>() {
                    }.getType());

            String usrgrpid = usrgrpids.get(0);
            if (!StringUtils.isEmpty(usrgrpid)) {
                logger.info("Zabbix usergroup {} 创建成功!", usergrpName);
                // TODO 自动创建告警动作
                createAction(serverGroupDO);
                return usrgrpid;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Zabbix usergroup {} 创建失败!", usergrpName);
        }
        return null;
    }


    @Override
    public String createAction(ServerGroupDO serverGroupDO) {
        ZabbixResponseAction action = getAction(serverGroupDO);
        if (action != null) return action.getActionid();
        //需要转换名称  group_name ==> users_name
        String usergrpName = serverGroupDO.getName().replace("group_", "users_");

        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("action.create").build();
        request.putParam("name", "Report problems to " + usergrpName);
        request.putParam("eventsource", 0);
        request.putParam("status", 0);
        //默认操作步骤持续时间。必须大于60秒。 4.0改了类型   request.putParam("esc_period", 3600);
        request.putParam("esc_period", "10m");
        request.putParam("def_shortdata", "{TRIGGER.NAME}: {TRIGGER.STATUS}");
        request.putParam("def_longdata", "ServerName:{HOST.NAME} IP:{HOST.IP}\r\nTrigger: {TRIGGER.NAME}\r\nTrigger severity: {TRIGGER.SEVERITY}\r\nItem values: {ITEM.NAME1} ({HOST.NAME1}:{ITEM.KEY1}): {ITEM.VALUE1}\r\nOriginal event ID: {EVENT.ID}");
        // operations 操作
        JSONArray operations = new JSONArray();
        JSONObject operation = new JSONObject();
        operation.put("operationtype", 0);
        // 4.0 版本修改   operation.put("esc_period", 0);
        operation.put("esc_period", "0s");
        operation.put("esc_step_from", 1);
        operation.put("esc_step_to", 1);
        operation.put("evaltype", 0);

        JSONArray opmessage_grp = new JSONArray();
        JSONObject usrgrp = new JSONObject();
        usrgrp.put("usrgrpid", getUsergroup(usergrpName).getUsrgrpid());
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

        /**
         * https://www.zabbix.com/documentation/4.0/manual/api/reference/action/object?s[]=conditions
         * 	触发器示警度
         */
        JSONObject conditionA = new JSONObject();
        conditionA.put("conditiontype", 4);
        conditionA.put("operator", 5);
        conditionA.put("value", "4");
        conditionA.put("formulaid", "A");
        conditions.add(conditionA);

        // 服务器组
        JSONObject conditionB = new JSONObject();
        conditionB.put("conditiontype", 0);
        conditionB.put("operator", 0);
        conditionB.put("value", getHostgroup(serverGroupDO.getName()).getGroupid());
        conditionB.put("formulaid", "B");
        conditions.add(conditionB);

        filter.put("conditions", conditions);
        request.putParam("filter", filter);
        try {
            JSONObject response = call(request);
            String reslut = response.getJSONObject(ZABBIX_KEY_RESULT).getJSONArray("actionids").toJSONString();
            Gson gson = new GsonBuilder().create();
            List<String> actionids = gson.fromJson(reslut,
                    new TypeToken<ArrayList<String>>() {
                    }.getType());
            return actionids.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public ZabbixResponseAction getAction(ServerGroupDO serverGroupDO) {
        if (serverGroupDO == null) return null;
        String usergrpName = serverGroupDO.getName().replace("group_", "users_");
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("action.get").build();
        request.putParam("output", "extend");
        request.putParam("selectOperations", "extend");

        JSONObject filter = new JSONObject();
        filter.put("name", "Report problems to " + usergrpName);
        request.putParam("filter", filter);

        JSONObject response = call(request);
        try {
            String reslut = response.getJSONArray(ZABBIX_KEY_RESULT).toJSONString();
            Gson gson = new GsonBuilder().create();
            List<ZabbixResponseAction> actionList = gson.fromJson(reslut,
                    new TypeToken<ArrayList<ZabbixResponseAction>>() {
                    }.getType());
            return actionList.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String createHostgroup(ServerDO serverDO) {
        String hostgroupName = getServerGroupName(serverDO);
        return createHostgroup(hostgroupName);
    }

    @Override
    public String createHostgroup(String hostgroupName) {
        ZabbixResponseHostgroup hostgroup = getHostgroup(hostgroupName);
        if (hostgroup != null && !StringUtils.isEmpty(hostgroup.getGroupid()))
            return hostgroup.getGroupid();
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder().method("hostgroup.create").paramEntry("name", hostgroupName).build();
        JSONObject response = call(request);
        try {
            String reslut = response.getJSONObject(ZABBIX_KEY_RESULT).getJSONArray("groupids").toJSONString();
            Gson gson = new GsonBuilder().create();
            List<String> groupids = gson.fromJson(reslut, new TypeToken<ArrayList<String>>() {
            }.getType());
            return groupids.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean createHost(ServerDO serverDO, ZabbixHost host) {
        if (existsHost(serverDO)) return true;
        String groupid = createHostgroup(serverDO);
        if (StringUtils.isEmpty(groupid))
            return false;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.create").build();
        request.putParam("host", serverDO.acqServerName());
        request.putParam("interfaces", acqInterfaces(serverDO));
        request.putParam("groups", acqGroup(serverDO));
        request.putParam("templates", acqTemplate(host.getTemplates()));
        String macrosStr = configServerGroupService.queryZabbixMacros(serverDO);
        if (!StringUtils.isEmpty(macrosStr)) {
            try {
                JSONArray macros = JSONArray.parseArray(macrosStr);
                if (macros.size() != 0)
                    request.putParam("macros", macros);
            } catch (Exception e) {
                logger.info("Zabbix host = {} , macros = {} macros配置错误!", serverDO.acqHostname(), macrosStr);
            }
        }
        // TODO 插入代理
        if (host.isUseProxy())
            if (host.getProxy() != null && !StringUtils.isEmpty(host.getProxy().getProxyid()))
                request.putParam("proxy_hostid", host.getProxy().getProxyid());
        try {
            JSONObject response = call(request);
            String reslut = response.getJSONObject(ZABBIX_KEY_RESULT).getJSONArray("hostids").toJSONString();
            Gson gson = new GsonBuilder().create();
            List<String> hostids = gson.fromJson(reslut, new TypeToken<ArrayList<String>>() {
            }.getType());
            String hostid = hostids.get(0);
            if (!StringUtils.isEmpty(hostid))
                return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean existsHost(ServerDO serverDO) {
        if (StringUtils.isEmpty(getHost(serverDO).getHostid()))
            return false;
        return true;
    }

//    /**
//     * 按IP查询主机
//     *
//     * @param ip
//     * @return
//     */
//    @
//    public int getHostByIP(String ip) {
//        JSONObject filter = new JSONObject();
//        filter.put("ip", ip);
//        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
//                .method("host.get").paramEntry("filter", filter)
//                .build();
//        JSONObject getResponse = call(request);
//       // return getResultId(getResponse, "hostid");
//    }
//
//    @Deprecated
//    public int getHostByName(String name) {
//        JSONObject filter = new JSONObject();
//        filter.put("host", name);
//        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
//                .method("host.get").paramEntry("filter", filter)
//                .build();
//        JSONObject getResponse = call(request);
//        // return getResultId(getResponse, "hostid");
//    }


    @Override
    public ZabbixResponseProxy getProxy(String hostname) {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("proxy.get")
                .build();
        if (hostname != null && !hostname.isEmpty()) {
            JSONObject filter = new JSONObject();
            filter.put("host", hostname);
            request.putParam("filter", filter);
        }
        JSONObject response = call(request);
        try {
            String reslut = response.getJSONArray(ZABBIX_KEY_RESULT).toJSONString();
            Gson gson = new GsonBuilder().create();
            List<ZabbixResponseProxy> proxyList = gson.fromJson(reslut, new TypeToken<ArrayList<ZabbixResponseProxy>>() {
            }.getType());
            return proxyList.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    private JSONArray acqTemplate(List<ZabbixTemplateVO> templates) {
        if (templates == null || templates.size() == 0) return new JSONArray();
        JSONArray templateArray = new JSONArray();
        for (ZabbixTemplateVO template : templates) {
            if (template.isChoose()) {
                //int  templateid = templateGet(template.getTemplateName());
                JSONObject t = new JSONObject();
                t.put("templateid", getTemplate(template.getTemplateName()).getTemplateid());
                templateArray.add(template);
            }

        }
        return templateArray;
    }

    private JSONArray acqGroup(ServerDO serverDO) {
        JSONArray jsonarray = new JSONArray();
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("groupid", getHostgroup(getServerGroupName(serverDO)).getGroupid());
        jsonarray.add(jsonobject);
        return jsonarray;
    }

    public boolean deleteHost(ServerDO serverDO) {
        if (serverDO == null) return false;
        ZabbixResponseHost host = getHost(serverDO);
        if (host == null) return true;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.delete").paramEntry("params", new String[]{host.getHostid()}).build();
        try {
            JSONObject response = call(request);
            String reslut = response.getJSONObject(ZABBIX_KEY_RESULT).getJSONArray("hostids").toJSONString();
            Gson gson = new GsonBuilder().create();
            List<String> hostids = gson.fromJson(reslut, new TypeToken<ArrayList<String>>() {
            }.getType());
            if (!StringUtils.isEmpty(hostids.get(0)))
                return true;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private JSONArray acqInterfaces(ServerDO serverDO) {
        if (serverDO == null) return null;
        JSONArray interfaces = new JSONArray();
        String ip = configServerGroupService.queryZabbixMonitorIp(serverDO);
        interfaces.add(buildInterfaces(ip, 1, "10050"));
        //interfaces.add(JSONObject.parseObject(new ZabbixInterface(1, ip, "10050").toString()));
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

    @Override
    public ZabbixResponseHostgroup getHostgroup(String name) {
        JSONObject filter = new JSONObject();
        filter.put("name", name);
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("hostgroup.get").paramEntry("filter", filter).paramEntry("output", "extend")
                .build();
        JSONObject response = call(request);
        try {
            String reslut = response.getJSONArray(ZABBIX_KEY_RESULT).toJSONString();
            Gson gson = new GsonBuilder().create();
            List<ZabbixResponseHostgroup> hostgroupList = gson.fromJson(reslut,
                    new TypeToken<ArrayList<ZabbixResponseHostgroup>>() {
                    }.getType());
            return hostgroupList.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean updateHostStatus(ServerDO serverDO, int status) {
        // int hostid = getHost(serverDO);
        ZabbixResponseHost host = getHost(serverDO);
        // 主机不存在
        if (host == null) return false;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.update").build();
        request.putParam("hostid", host.getHostid());
        request.putParam("status", status);
        JSONObject response = call(request);
        try {
            String reslut = response.getJSONObject(ZABBIX_KEY_RESULT).getJSONArray("hostids").toJSONString();
            Gson gson = new GsonBuilder().create();
            List<String> hostids = gson.fromJson(reslut, new TypeToken<ArrayList<String>>() {
            }.getType());
            if (!StringUtils.isEmpty(hostids.get(0)))
                return true;
        } catch (Exception e) {

        }
        return false;
    }


    @Override
    public ZabbixResponseUser getUser(UserDO userDO) {
        if (userDO == null) return null;
        JSONObject filter = new JSONObject();
        filter.put("alias", userDO.getUsername());
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("user.get").paramEntry("filter", filter)
                .build();
        try {
            JSONObject response = call(request);
            String reslut = response.getJSONArray(ZABBIX_KEY_RESULT).toJSONString();
            Gson gson = new GsonBuilder().create();
            List<ZabbixResponseUser> userList = gson.fromJson(reslut,
                    new TypeToken<ArrayList<ZabbixResponseUser>>() {
                    }.getType());

            return userList.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<ZabbixResponseUser> queryUser() {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("user.get")
                .build();
        try {
            JSONObject response = call(request);
            String reslut = response.getJSONArray(ZABBIX_KEY_RESULT).toJSONString();
            Gson gson = new GsonBuilder().create();
            List<ZabbixResponseUser> userList = gson.fromJson(reslut,
                    new TypeToken<ArrayList<ZabbixResponseUser>>() {
                    }.getType());
            return userList;
        } catch (Exception e) {
            return new ArrayList<ZabbixResponseUser>();
        }
    }

    @Override
    public BusinessWrapper<Boolean> deleteUser(UserDO userDO) {
        if (userDO == null)
            return new BusinessWrapper<>(ErrorCode.userNotExist.getCode(), ErrorCode.userNotExist.getMsg());
        try {
            ZabbixResponseUser user = getUser(userDO);
            if (user == null || StringUtils.isEmpty(user.getUserid())) return new BusinessWrapper<>(true);
            // 数组形参数 https://www.zabbix.com/documentation/2.2/manual/api/reference/user/delete
            ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                    .method("user.delete").paramEntry("params", new String[]{
                            user.getUserid()
                    }).build();
            JSONObject response = call(request);
            String reslut = response.getJSONObject(ZABBIX_KEY_RESULT).getJSONArray("userids").toJSONString();
            Gson gson = new GsonBuilder().create();
            List<String> userids = gson.fromJson(reslut, new TypeToken<ArrayList<String>>() {
            }.getType());

            String userid = userids.get(0);

            if (StringUtils.isEmpty(userid))
                return new BusinessWrapper<>(ErrorCode.zabbixUserDelete.getCode(), ErrorCode.zabbixUserDelete.getMsg());
            userDO.setZabbixAuthed(UserDO.ZabbixAuthType.noAuth.getCode());
            userDao.updateUserZabbixAuthed(userDO);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> createUser(UserDO userDO) {
        if (userDO == null)
            return new BusinessWrapper<>(ErrorCode.userNotExist.getCode(), ErrorCode.userNotExist.getMsg());
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("user.create").build();
        request.putParam("alias", userDO.getUsername());
        request.putParam("name", userDO.getDisplayName());
        request.putParam("passwd", userDO.getPwd());
        request.putParam("usrgrps", acqUsergrps(userDO));
        JSONArray userMedias = new JSONArray();
        userMedias.add(acqUserMedias(1, userDO.getMail()));
        if (userDO.getMobile() != null && !userDO.getMobile().isEmpty()) {
            userMedias.add(acqUserMedias(3, userDO.getMobile()));
        }
        request.putParam("user_medias", userMedias);
        try {
            JSONObject response = call(request);
            System.err.println(request);
            System.err.println(response);
            String reslut = response.getJSONObject(ZABBIX_KEY_RESULT).getJSONArray("userids").toJSONString();
            Gson gson = new GsonBuilder().create();
            List<String> usrgrpids = gson.fromJson(reslut,
                    new TypeToken<ArrayList<String>>() {
                    }.getType());
            String userid = usrgrpids.get(0);
            if (!StringUtils.isEmpty(userid)) {
                userDO.setZabbixAuthed(UserDO.ZabbixAuthType.authed.getCode());
                userDao.updateUserZabbixAuthed(userDO);
                return new BusinessWrapper<>(true);
            }
            userDO.setZabbixAuthed(UserDO.ZabbixAuthType.noAuth.getCode());
            userDao.updateUserZabbixAuthed(userDO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<>(ErrorCode.zabbixUserCreate.getCode(), ErrorCode.zabbixUserCreate.getMsg());
    }

    @Override
    public BusinessWrapper<Boolean> updateUser(UserDO userDO) {
        if (userDO == null)
            return new BusinessWrapper<>(ErrorCode.userNotExist.getCode(), ErrorCode.userNotExist.getMsg());
        ZabbixResponseUser zabbixResponseUser = getUser(userDO);
        if(zabbixResponseUser == null || StringUtils.isEmpty(zabbixResponseUser.getUserid()))
            return new BusinessWrapper<Boolean>(false);
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("user.update").build();
        request.putParam("userid", getUser(userDO).getUserid());
        request.putParam("name", userDO.getDisplayName());
        request.putParam("usrgrps", acqUsergrps(userDO));
        JSONArray userMedias = new JSONArray();
        userMedias.add(acqUserMedias(1, userDO.getMail()));
        if (userDO.getMobile() != null && !userDO.getMobile().isEmpty()) {
            userMedias.add(acqUserMedias(3, userDO.getMobile()));
        }
        request.putParam("user_medias", userMedias);
        try {
            JSONObject response = call(request);
            String reslut = response.getJSONObject(ZABBIX_KEY_RESULT).getJSONArray("userids").toJSONString();
            Gson gson = new GsonBuilder().create();
            List<String> usrgrpids = gson.fromJson(reslut,
                    new TypeToken<ArrayList<String>>() {
                    }.getType());
            String userid = usrgrpids.get(0);
            if (!StringUtils.isEmpty(userid)) {
                userDO.setZabbixAuthed(UserDO.ZabbixAuthType.authed.getCode());
                userDao.updateUserZabbixAuthed(userDO);
                return new BusinessWrapper<>(true);
            }
            userDO.setZabbixAuthed(UserDO.ZabbixAuthType.noAuth.getCode());
            userDao.updateUserZabbixAuthed(userDO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<>(ErrorCode.zabbixUserCreate.getCode(), ErrorCode.zabbixUserCreate.getMsg());
    }

    @Override
    public ZabbixResponseItem getItem(ServerDO serverDO, String itemName, String itemKey) {
        if (serverDO == null) return null;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("item.get").build();
        request.putParam("output", "extend");
        request.putParam("hostids", getHost(serverDO).getHostid());
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
        try {
            JSONObject response = call(request);
            String reslut = response.getJSONArray(ZABBIX_KEY_RESULT).toJSONString();
            Gson gson = new GsonBuilder().create();
            List<ZabbixResponseItem> itemList = gson.fromJson(reslut,
                    new TypeToken<ArrayList<ZabbixResponseItem>>() {
                    }.getType());
            return itemList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ZabbixResponseItem> queryItems(ServerDO serverDO) {
        if (serverDO == null) return null;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("item.get").build();
        request.putParam("output", "extend");
        request.putParam("hostids", getHost(serverDO).getHostid());
        request.putParam("sortfield", "name");
        try {
            JSONObject response = call(request);
            String reslut = response.getJSONArray(ZABBIX_KEY_RESULT).toJSONString();
            Gson gson = new GsonBuilder().create();
            List<ZabbixResponseItem> itemList = gson.fromJson(reslut,
                    new TypeToken<ArrayList<ZabbixResponseItem>>() {
                    }.getType());
            return itemList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<ZabbixResponseItem>();
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

    private JSONArray acqUsergrps(UserDO userDO) {
        List<ServerGroupDO> list = keyboxDao.getGroupListByUsername(userDO.getUsername());
        JSONArray groups = new JSONArray();
        JSONObject defaultUsergrp = new JSONObject();
        defaultUsergrp.put("usrgrpid", getUsergroupDefault());
        groups.add(defaultUsergrp);
        if (list != null && list.size() != 0) {
            for (ServerGroupDO serverGroupDO : list) {
                // TODO 默认会先get有则返回id
                String usrgrpid = createUsergroup(serverGroupDO);
                if (usrgrpid == null) continue;
                JSONObject defaultGroup = new JSONObject();
                defaultGroup.put("usrgrpid", getUsergroupDefault());
                JSONObject usergrp = new JSONObject();
                usergrp.put("usrgrpid", usrgrpid);
                groups.add(usergrp);
                new JSONObject();
            }
        }
        System.err.println(groups);
        return groups;
    }

    /**
     * 查询默认用户组 users_default
     *
     * @return
     */
    private String getUsergroupDefault() {
        if (!StringUtils.isEmpty(defaultUsergroupId))
            return defaultUsergroupId;
        String usergroupName = "users_default";
        String hostgroupName = "group_default";
        ZabbixResponseUsergroup zabbixUsergroup = getUsergroup(usergroupName);
        if (zabbixUsergroup != null && !StringUtils.isEmpty(zabbixUsergroup.getUsrgrpid())) {
            defaultUsergroupId = zabbixUsergroup.getUsrgrpid();
            return zabbixUsergroup.getUsrgrpid();
        }
        defaultUsergroupId = createUsergroup(new ServerGroupDO(hostgroupName));
        return defaultUsergroupId;
    }

    @Override
    public List<ZabbixResponseProxy> queryProxys() {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("proxy.get")
                .build();
        JSONObject response = call(request);
        String reslut = response.getJSONArray(ZABBIX_KEY_RESULT).toJSONString();
        Gson gson = new GsonBuilder().create();
        List<ZabbixResponseProxy> templateList = gson.fromJson(reslut,
                new TypeToken<ArrayList<ZabbixResponseProxy>>() {
                }.getType());
        return templateList;
    }

    /**
     * @param userDO
     * @param serverGroupDO
     * @return 2:zabbix用户不存在  3:用户组不存在
     */
    @Override
    public int checkUserInUsergroup(UserDO userDO, ServerGroupDO serverGroupDO) {
        ZabbixResponseUser zabbixUser = getUser(userDO);
        if (zabbixUser == null || StringUtils.isEmpty(zabbixUser.getUserid()))
            return 2;
        ZabbixResponseUsergroup zabbixUsergroup = getUsergroup(serverGroupDO.getName().replace("group_", "users_"));
        if (zabbixUsergroup == null || StringUtils.isEmpty(zabbixUsergroup.getUsrgrpid()))
            return 3;
        // 数组形参数
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("user.get").paramEntry("userids", new String[]{
                        zabbixUser.getUserid()
                }).paramEntry("usrgrpids", new String[]{
                        zabbixUsergroup.getUsrgrpid()
                })
                .build();
        try {
            JSONObject response = call(request);
            String reslut = response.getJSONArray(ZABBIX_KEY_RESULT).toJSONString();
            Gson gson = new GsonBuilder().create();
            List<ZabbixResponseUser> userList = gson.fromJson(reslut,
                    new TypeToken<ArrayList<ZabbixResponseUser>>() {
                    }.getType());
            ZabbixResponseUser checkZabbixUser = userList.get(0);
            if (checkZabbixUser != null && !StringUtils.isEmpty(checkZabbixUser.getUserid()))
                return 1;
        } catch (Exception e) {

        }
        return 0;
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
    public JSONObject getHistory(ServerDO serverDO, String itemName, String itemKey, int historyType, int limit) {
        if (serverDO == null) return null;
        ZabbixResponseItem item = getItem(serverDO, itemName, itemKey);
        if (item == null || StringUtils.isEmpty(item.getItemid()))
            return null;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("history.get").build();
        request.putParam("history", historyType);
        request.putParam("itemids", item.getItemid());
        request.putParam("sortfield", "clock");
        request.putParam("sortorder", "DESC");
        request.putParam("limit", limit);
        return call(request);
    }

    @Override
    public JSONObject getHistory(ServerDO serverDO, String itemName, String itemKey, int historyType, String timestampFrom, String timestampTill) {
        if (serverDO == null) return null;
        ZabbixResponseItem item = getItem(serverDO, itemName, itemKey);
        if (item == null || StringUtils.isEmpty(item.getItemid()))
            return null;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("history.get").build();
        request.putParam("history", historyType);
        request.putParam("itemids", item.getItemid());
        request.putParam("sortfield", "clock");
        request.putParam("sortorder", "DESC");
        request.putParam("time_from", timestampFrom);
        request.putParam("time_till", timestampTill);
        return call(request);
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
        JSONObject response = call(request);
        ZabbixVersion version = new ZabbixVersion();
        version.setVersion(getApiVersion());
        try {
            JSONObject result = response.getJSONArray(ZABBIX_KEY_RESULT).getJSONObject(0);
            String hostid = result.getString("hostid");
            version.setHostid(hostid);
            String name = result.getString("name");
            version.setName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    private String getApiVersion() {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder().method("apiinfo.version").build();
        JSONObject response = call(request);
        return response.getString("result");
    }

    private String getServerGroupName(ServerDO serverDO) {
        if (serverDO == null) return null;
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverDO.getServerGroupId());
        return serverGroupDO.getName();
    }

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
     * @return
     */
    public boolean login() {
        if (checkAuth()) return true;
        if (StringUtils.isEmpty(zabbixUrl))
            return false;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder().paramEntry("user", zabbixUser).paramEntry("password", zabbixPasswd)
                .method("user.login").build();
        try {
            HttpUriRequest httpRequest = org.apache.http.client.methods.RequestBuilder.post().setUri(uri)
                    .addHeader("Content-Type", "application/json")
                    .setEntity(new StringEntity(JSON.toJSONString(request), ContentType.APPLICATION_JSON)).build();
            CloseableHttpResponse response = httpClient.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            byte[] data = EntityUtils.toByteArray(entity);
            Gson gson = new GsonBuilder().create();
            ZabbixResponseUserLogin userLogin = gson.fromJson(JSON.parse(data).toString(), ZabbixResponseUserLogin.class);
            if (!StringUtils.isEmpty(userLogin.getAuth())) {
                logger.info("Zabbix登录成功 !");
                this.auth = userLogin.getAuth();
                cacheZabbixService.insertZabbixAuth(auth);
                return true;
            }
        } catch (IOException e) {
            logger.error("zabbix登陆失败!");
        }
        return false;
    }

    /**
     * 从redis获取auth，并校验auth是否过期
     *
     * @return
     */
    public boolean checkAuth() {
        if (StringUtils.isEmpty(zabbixUrl))
            return false;
        String auth = cacheZabbixService.getZabbixAuth();
        if (!StringUtils.isEmpty(auth)) {
            this.auth = auth;
            JSONObject filter = new JSONObject();
            filter.put("host", ZABBIX_SERVER_DEFAULT_NAME);
            ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                    .method("host.get").paramEntry("filter", filter)
                    .build();
            try {
                JSONObject response = call(request);
                Gson gson = new GsonBuilder().create();
                List<ZabbixResponseHost> hostList = gson.fromJson(response.getJSONArray(ZABBIX_KEY_RESULT).toString(), new TypeToken<ArrayList<ZabbixResponseHost>>() {
                }.getType());
                for (ZabbixResponseHost host : hostList)
                    if (!StringUtils.isEmpty(host.getHostid())) return true;
            } catch (Exception e) {
                logger.info("Zabbix校验auth失败！");
            }
        }
        return false;
    }

    private JSONObject call(ZabbixRequest request) {
        if (request.getAuth() == null && !request.getMethod().equalsIgnoreCase(ZABBIX_KEY_APIINFO) && !request.getMethod().equalsIgnoreCase("user.login")) {
            if (StringUtils.isEmpty(auth)) return null;
            request.setAuth(auth);
        }
        try {
            HttpUriRequest httpRequest = org.apache.http.client.methods.RequestBuilder.post().setUri(uri)
                    .addHeader("Content-Type", "application/json")
                    .setEntity(new StringEntity(JSON.toJSONString(request), ContentType.APPLICATION_JSON)).build();
            CloseableHttpResponse response = httpClient.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            byte[] data = EntityUtils.toByteArray(entity);
            JSONObject jsonObject = JSONObject.parseObject(JSON.parse(data).toString());
            return jsonObject;
        } catch (IOException e) {
            logger.error("Zabbix接口错误!");
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO url不填则不启用Zabbix
        if (StringUtils.isEmpty(zabbixUrl))
            return;
        logger.info("Zabbix初始化 : {}", zabbixUrl);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5 * 1000).setConnectionRequestTimeout(5 * 1000)
                .setSocketTimeout(5 * 1000).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig).build();
        setUrl(zabbixUrl);
        setHttpClient(httpclient);
        login();
    }

}
