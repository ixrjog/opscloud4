package com.sdg.cmdb.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.*;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.GetHistogramsRequest;
import com.aliyun.openservices.log.request.GetLogsRequest;
import com.aliyun.openservices.log.request.ListLogStoresRequest;
import com.aliyun.openservices.log.response.BatchGetLogResponse;
import com.aliyun.openservices.log.response.GetCursorResponse;
import com.aliyun.openservices.log.response.GetHistogramsResponse;
import com.aliyun.openservices.log.response.GetLogsResponse;
import com.sdg.cmdb.dao.cmdb.LogServiceDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.AliyunEcsItemEnum;
import com.sdg.cmdb.domain.logService.*;
import com.sdg.cmdb.domain.keybox.KeyboxUserServerDO;
import com.sdg.cmdb.domain.logService.*;
import com.sdg.cmdb.domain.logService.logServiceQuery.*;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerGroupVO;
import com.sdg.cmdb.service.AliyunLogService;
import com.sdg.cmdb.service.AuthService;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.service.KeyBoxService;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.TimeUtils;
import com.sdg.cmdb.util.TimeViewUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liangjian on 2017/9/18.
 */
@Service(value = "aliyunLogService")
public class AliyunLogServiceImpl implements AliyunLogService {

    static final String ALIYUN_LOG_ENDPOINT = "cn-hangzhou.log.aliyuncs.com";

    @Resource
    private ConfigCenterService configCenterService;

    @Resource
    private LogServiceDao logServiceDao;

    @Resource
    private ServerDao serverDao;

    @Resource
    private UserDao userDao;

    @Resource
    private KeyBoxService keyBoxService;

    @Resource
    private AuthService authService;

    @Resource
    private ServerGroupDao serverGroupDao;

    private HashMap<String, String> configMap;

    public static final String PROJECT_JAVA_LOG = "collect-web-service-logs";

    static private Client client;

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.ALIYUN_ECS.getItemKey());
    }

    @Override
    public TableVO<List<LogServiceCfgDO>> getLogServiceCfgPage(int page, int length, String serverName) {
        long size = 0;
        List<LogServiceCfgDO> list = logServiceDao.queryLogServiceCfgPage(page * length, length, serverName);
        return new TableVO<>(size, list);
    }


//    @Override
//    public LogServiceVO queryNginxLog(LogServiceKaQuery logServiceKaQuery) throws LogException {
//        return queryLog(logServiceKaQuery);
//    }
//
//    @Override
//    public LogServiceVO queryJavaLog(LogServiceDefaultQuery logServiceDefaultQuery) throws LogException {
//        return queryLog(logServiceDefaultQuery);
//    }


    @Override
    public LogServiceVO queryLog(LogServiceQuery logServiceQuery) throws LogException {

        try {
            //String date = logServiceQuery.acqQueryBeginDate();
            long queryFrom = TimeUtils.dateToStamp(logServiceQuery.acqQueryBeginDate());
            int from = (int) (queryFrom / 1000);
            long queryTo = TimeUtils.dateToStamp(logServiceQuery.acqQueryEndDate());
            int to = (int) (queryTo / 1000);

            LogServiceQueryCfg cfg = logServiceQuery.acqLogServiceQueryCfg();
            String query = acqQuery(logServiceQuery);
            System.err.println("query='" + query +"'");

            if (from > to) return new LogServiceVO();

            String project = cfg.acqProject();
            String logstore = cfg.acqLogstore();
            String topic = cfg.acqTopic();

            GetHistogramsResponse res = queryHistograms(project, logstore, topic, from, to, query);
            // ArrayList<Histogram> histograms

            //System.out.println("Total count of logs is " + res.GetTotalCount());
            UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
            LogServiceDO logServiceDO = new LogServiceDO(cfg, userDO, query, from, to);
            logServiceDO.setTotalCount((int) res.GetTotalCount());
            logServiceDao.addLogService(logServiceDO);
            long logServiceId = logServiceDO.getId();
            //List<LogHistogramsVO> histogramsVOList = new ArrayList<>();

            int histogramsCnt = 0;
            for (Histogram ht : res.GetHistograms()) {
                if (ht.GetCount() == 0l) continue;
                //System.out.printf("from %d, to %d, count %d.\n", ht.GetFrom(), ht.GetTo(), ht.GetCount());
                //queryKaLog(project, logstore, topic, query, ht);
                LogHistogramsDO logHistogramsDO = new LogHistogramsDO(ht, logServiceId);
                logServiceDao.addLogHistograms(logHistogramsDO);

                //String gmtFrom = TimeUtils.stampSecToDate(String.valueOf(logHistogramsDO.getTimeFrom()));
                //String gmtTo = TimeUtils.stampSecToDate(String.valueOf(logHistogramsDO.getTimeTo()));
                //histogramsVOList.add(new LogHistogramsVO(logHistogramsDO, gmtFrom, gmtTo));
                histogramsCnt++;
            }
            String logServiceGmtFrom = TimeUtils.stampSecToDate(String.valueOf(logServiceDO.getTimeFrom()));
            String logServiceGmtTo = TimeUtils.stampSecToDate(String.valueOf(logServiceDO.getTimeTo()));

            LogServiceVO logServiceVO = new LogServiceVO(logServiceDO, logServiceGmtFrom, logServiceGmtTo);

            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
                Date createDate = format.parse(logServiceDO.getGmtCreate());
                logServiceVO.setTimeView(TimeViewUtils.format(createDate));
            } catch (Exception e) {
            }
            logServiceVO.setHistogramsCnt(histogramsCnt);
            invokeLogServicePath(logServiceVO, logServiceQuery);
            return logServiceVO;

        } catch (Exception e) {
            e.printStackTrace();
            return new LogServiceVO();
        }

    }

    private void invokePath(HashMap<String, Long> map) {
        Iterator iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String tagPath = (String) iter.next();
            Long serverGroupId = map.get(tagPath);
            saveLogServicePath(new LogServicePathDO(tagPath, serverGroupId), 0);
        }
    }

    /**
     * 保存或初始化path
     *
     * @param logServicePathDO
     * @param type             0 初始化  1 更新
     * @return
     */
    private boolean saveLogServicePath(LogServicePathDO logServicePathDO, int type) {
        System.err.println(logServicePathDO);
        System.err.println(type);
        LogServicePathDO logServicePath = logServiceDao.queryLogServicePath(logServicePathDO.getTagPath(), logServicePathDO.getServerGroupId());
        try {
            if (logServicePath != null) {
                logServicePath.setSearchCnt(logServicePath.getSearchCnt() + type);
                logServiceDao.updateLogServicePath(logServicePath);
            } else {
                logServicePathDO.setSearchCnt(type);
                logServiceDao.addLogServicePath(logServicePathDO);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private void invokeLogServicePath(LogServiceVO logServiceVO, LogServiceQuery logServiceQuery) {
        // 非java类查询
        if (logServiceQuery.acqQueryType() != 1) return;
        // 无查询结果，可能是误查
        if (logServiceVO.getHistogramsCnt() == 0) return;
        LogServiceDefaultQuery query = (LogServiceDefaultQuery) logServiceQuery;
        if (StringUtils.isEmpty(query.getTagPath()))
            return;
        String tagPath = query.getTagPath();
        long serverGroupId = query.getLogServiceServerGroupCfg().getServerGroupId();

        saveLogServicePath(new LogServicePathDO(tagPath, serverGroupId), 1);

    }


    private String acqQuery(LogServiceQuery query) {
        switch (query.acqQueryType()) {
            case 0:
                return acqQuery((LogServiceKaQuery) query);
            case 1:
                return acqQuery((LogServiceDefaultQuery) query);
            default:
                return "";

        }
    }

    // 默认查询java服务器日志
    private String acqQuery(LogServiceDefaultQuery defaultQuery) {
        String query = "";
        // 若用户选中logServicePath则从此处获取tagPath
        if (!StringUtils.isEmpty(defaultQuery.getLogServicePath().getTagPath())) {
            defaultQuery.setTagPath(defaultQuery.getLogServicePath().getTagPath());
        } else {
            // 去除空格
            String tagPath = defaultQuery.getTagPath().replace(" ", "");
            defaultQuery.setTagPath(tagPath);
        }

        //query += LogServiceDefaultQuery.TOPIC_KEY + ":" + defaultQuery.getLogServiceServerGroupCfg().getTopic();

        if (!StringUtils.isEmpty(defaultQuery.getTagPath())) {
            query += (StringUtils.isEmpty(query)) ? "" : " and ";
            query += LogServiceDefaultQuery.TAG_PATH_KEY + ":" + defaultQuery.getTagPath();
        }

        if (!StringUtils.isEmpty(defaultQuery.getSearch())) {
            query += (StringUtils.isEmpty(query)) ? "" : " and ";
            query += "content = " + defaultQuery.getSearch();
        }
        return query;
    }

    // nginx日志查询
    private String acqQuery(LogServiceKaQuery kaQuery) {
        String query = "";
        if (!StringUtils.isEmpty(kaQuery.getArgs())) {
            query += "args = " + kaQuery.getArgs();
        }
        if (!StringUtils.isEmpty(kaQuery.getUri())) {
            query += (StringUtils.isEmpty(query)) ? "" : " and ";
            query += "uri = " + kaQuery.getUri();
        }
        if (!StringUtils.isEmpty(kaQuery.getMobile())) {
            query += (StringUtils.isEmpty(query)) ? "" : " and ";
            query += "mobile = " + kaQuery.getMobile();
        }
        if (!StringUtils.isEmpty(kaQuery.getStatus())) {
            query += (StringUtils.isEmpty(query)) ? "" : " and ";
            query += "status = " + kaQuery.getStatus();
        }
        if (!StringUtils.isEmpty(kaQuery.getSourceIp())) {
            query += (StringUtils.isEmpty(query)) ? "" : " and ";
            query += "sourceIp = " + kaQuery.getSourceIp();
        }
        if (!StringUtils.isEmpty(kaQuery.getRequestTime())) {
            query += (StringUtils.isEmpty(query)) ? "" : " and ";
            query += "requestTime >= " + kaQuery.getRequestTime();
        }
        return query;
    }


    public GetHistogramsResponse queryHistograms(String project, String logstore, String topic, int from, int to, String query) throws LogException {
        Client client = acqClient();
        // 查询日志分布情况
        //int from = (int) (new Date().getTime() / 1000 - 300);
        //int to = (int) (new Date().getTime() / 1000);
        GetHistogramsResponse res = null;
        while (true) {
            GetHistogramsRequest req = new GetHistogramsRequest(project, logstore, topic, query, from, to);
            res = client.GetHistograms(req);
            if (res != null && res.IsCompleted()) // IsCompleted() 返回
            // true，表示查询结果是准确的，如果返回
            // false，则重复查询
            {
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    @Override
    public TableVO<List<LogServicePathDO>> getLogServicePathPage(int page, int length, String tagPath, long serverGroupId) {
        long size = logServiceDao.getLogServicePathSize(tagPath, serverGroupId);
        List<LogServicePathDO> list = logServiceDao.queryLogServicePathPage(page * length, length, tagPath, serverGroupId);
        return new TableVO<>(size, list);
    }


    private ArrayList<QueriedLog> queryLog(LogHistogramsVO logHistogramsVO, LogServiceDO logServiceDO) throws LogException {
        int log_offset = 0;
        int log_line = 10;
        if (logHistogramsVO.getTotalCount() <= 100)
            log_line = logHistogramsVO.getTotalCount();

        //LogServiceDO logServiceDO = logServiceDao.queryLogServiceById(logHistogramsVO.getLogServiceId());
        String query = logServiceDO.getQuery();
        String project = logServiceDO.getProject();
        String logstore = logServiceDO.getLogstore();
        String topic = logServiceDO.getTopic();
        int from = logHistogramsVO.getTimeFrom();
        int to = logHistogramsVO.getTimeTo();
        ArrayList<QueriedLog> logs = new ArrayList<>();
        while (log_offset <= logHistogramsVO.getTotalCount()) {
            GetLogsResponse res2 = null;
            // 对于每个 log offset,一次读取 10 行 log，如果读取失败，最多重复读取 3 次。
            for (int retry_time = 0; retry_time < 3; retry_time++) {
                GetLogsRequest req2 = new GetLogsRequest(project, logstore, from, to, topic, query, log_offset,
                        log_line, false);
                res2 = client.GetLogs(req2);
                if (res2 != null && res2.IsCompleted()) {
                    break;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logs.addAll(res2.GetLogs());
            log_offset += log_line;
        }
        return logs;
    }


    @Override
    public TableVO<List<LogFormatDefault>> queryDefaultLog(LogHistogramsVO logHistogramsVO) throws LogException {

        LogServiceDO logServiceDO = logServiceDao.queryLogServiceById(logHistogramsVO.getLogServiceId());
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName(logServiceDO.getTopic());

        ArrayList<QueriedLog> logs = queryLog(logHistogramsVO, logServiceDO);
        List<LogFormatDefault> list = new ArrayList<>();
        HashMap<String, Long> map = new HashMap<>();

        for (QueriedLog queriedLog : logs) {
            // System.err.println(queriedLog.GetLogItem().ToJsonString());
            // System.err.println("source:"+queriedLog.GetSource());

            //JSONObject jsStr = JSONObject.parseObject(queriedLog.GetLogItem().ToJsonString());
            //System.err.println(jsStr);
            //LogFormatDefault lf = (LogFormatDefault) JSONObject.toJavaObject(jsStr, LogFormatDefault.class);
            LogFormatDefault lf = new LogFormatDefault();

            lf.setSource(queriedLog.GetSource());
            ServerDO serverDO = serverDao.queryServerByInsideIp(lf.getSource());
            if (serverDO != null)
                lf.setServerDO(serverDO);
            try {
                lf.setGmtLogtime(TimeUtils.stampSecToDate(String.valueOf(queriedLog.mLogItem.GetTime())));
            } catch (Exception e) {

            }
            for (LogContent content : queriedLog.mLogItem.GetLogContents()) {
                if (content.GetKey().equalsIgnoreCase(LogServiceDefaultQuery.TAG_PATH_KEY)) {
                    lf.setPath(content.GetValue());
                    map.put(content.GetValue(), serverGroupDO.getId());
                }
                if (content.GetKey().equalsIgnoreCase(LogServiceDefaultQuery.CONTENT_KEY))
                    lf.setContent(content.GetValue());
                // __tag__:__hostname__
                //JSONObject jsStr2 = JSONObject.parseObject(queriedLog.GetLogItem().ToJsonString());
                //LogFormatKaDO lfKa = (LogFormatKaDO) JSONObject.toJavaObject(jsStr,LogFormatKaDO.class);
                //System.err.println("KEY=\'" + content.GetKey() + "\' ; VALUE=\'" + content.GetValue() + "\'");
            }

            list.add(lf);

        }
        invokePath(map);
        return new TableVO<>(list.size(), list);
    }


    @Override
    public TableVO<List<LogFormatKa>> queryKaLog(LogHistogramsVO logHistogramsVO) throws LogException {
        LogServiceDO logServiceDO = logServiceDao.queryLogServiceById(logHistogramsVO.getLogServiceId());
        ArrayList<QueriedLog> logs = queryLog(logHistogramsVO, logServiceDO);
        List<LogFormatKa> list = new ArrayList<>();
        for (QueriedLog queriedLog : logs) {
            JSONObject jsStr = JSONObject.parseObject(queriedLog.GetLogItem().ToJsonString());
            LogFormatKa lfKa = (LogFormatKa) JSONObject.toJavaObject(jsStr, LogFormatKa.class);
            if (!StringUtils.isEmpty(lfKa.getUpstreamAddr())) {
                String ip = lfKa.getUpstreamAddr().split(":")[0];
                ServerDO serverDO = serverDao.queryServerByInsideIp(ip);
                if (serverDO != null)
                    lfKa.setServerDO(serverDO);
            }
            try {
                lfKa.setGmtLogtime(TimeUtils.stampSecToDate(String.valueOf(queriedLog.mLogItem.GetTime())));
            } catch (Exception e) {

            }
            list.add(lfKa);
        }
        return new TableVO<>(list.size(), list);
    }

    @Override
    public TableVO<List<LogFormatWww>> queryWwwLog(LogHistogramsVO logHistogramsVO) throws LogException {
        LogServiceDO logServiceDO = logServiceDao.queryLogServiceById(logHistogramsVO.getLogServiceId());
        ArrayList<QueriedLog> logs = queryLog(logHistogramsVO, logServiceDO);
        List<LogFormatWww> list = new ArrayList<>();
        for (QueriedLog queriedLog : logs) {
            JSONObject jsStr = JSONObject.parseObject(queriedLog.GetLogItem().ToJsonString());
            LogFormatWww lf = (LogFormatWww) JSONObject.toJavaObject(jsStr, LogFormatWww.class);
            if (!StringUtils.isEmpty(lf.getUpstreamAddr())) {
                String ip = lf.getUpstreamAddr().split(":")[0];
                ServerDO serverDO = serverDao.queryServerByInsideIp(ip);
                if (serverDO != null)
                    lf.setServerDO(serverDO);
            }
            try {
                lf.setGmtLogtime(TimeUtils.stampSecToDate(String.valueOf(queriedLog.mLogItem.GetTime())));
            } catch (Exception e) {

            }
            list.add(lf);
        }
        return new TableVO<>(list.size(), list);
    }

    @Override
    public Object queryNginxLog(LogHistogramsVO logHistogramsVO) throws LogException {
        LogServiceDO logServiceDO = logServiceDao.queryLogServiceById(logHistogramsVO.getLogServiceId());
        if (logServiceDO.getLogstore().equalsIgnoreCase("www")) {
            return queryWwwLog(logHistogramsVO);
        }
        if (logServiceDO.getLogstore().equalsIgnoreCase("ka-www") || logServiceDO.getLogstore().equalsIgnoreCase("ka-gray")) {
            return queryKaLog(logHistogramsVO);
        }
        return new Object();
    }


    public void readLog(String project, String logstore) {
        Client client = acqClient();
        int shard_id = 0;
        long curTimeInSec = System.currentTimeMillis() / 1000;
        try {
            GetCursorResponse cursorRes = client.GetCursor(project, logstore, shard_id, curTimeInSec - 3600);
            String beginCursor = cursorRes.GetCursor();
            cursorRes = client.GetCursor(project, logstore, shard_id, Consts.CursorMode.END);
            String endCursor = cursorRes.GetCursor();
            String curCursor = beginCursor;
            while (curCursor.equals(endCursor) == false) {
                int loggroup_count = 2; // 每次读取两个 loggroup
                BatchGetLogResponse logDataRes = client.BatchGetLog(project, logstore, shard_id, loggroup_count, curCursor,
                        endCursor);
                // 读取LogGroup的List
                List<LogGroupData> logGroups = logDataRes.GetLogGroups();
                for (LogGroupData logGroupData : logGroups) {
                    // 直接使用Protocol buffer格式的LogGroup进行
                    Logs.LogGroup log_group_pb = logGroupData.GetLogGroup();
                    //System.out.println("Source:" + log_group_pb.getSource());
                    // System.out.println("Topic:" + log_group_pb.getTopic());
                    for (Logs.Log log_pb : log_group_pb.getLogsList()) {
                        //   System.out.println("LogTime:" + log_pb.getTime());
                        for (Logs.Log.Content content : log_pb.getContentsList()) {
                            //    System.err.println(content.getKey() + ":" + content.getValue());
                        }
                    }
                }
                String next_cursor = logDataRes.GetNextCursor();
                //System.err.println("The Next cursor:" + next_cursor);
                curCursor = next_cursor;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() throws LogException, InterruptedException {
        // AccessKeySecret
        String project = "collect-nginx-logs"; // 上面步骤创建的项目名称
        String logstore = "ka-gray"; // 上面步骤创建的日志库名称
        // 构建一个客户端实例
        Client client = acqClient();
        // 列出当前 project 下的所有日志库名称
        int offset = 0;
        int size = 100;
        String logStoreSubName = "";
        ListLogStoresRequest req1 = new ListLogStoresRequest(project, offset, size, logStoreSubName);
        ArrayList<String> logStores = client.ListLogStores(req1).GetLogStores();
        //System.out.println("ListLogs:" + logStores.toString() + "\n");
    }

    @Override
    public Client acqClient() {
        if (client != null) return client;
        HashMap<String, String> configMap = acqConifMap();
        String accessKeyId = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_ACCESS_KEY.getItemKey());
        String accessKeySecret = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_ACCESS_SECRET.getItemKey());
        // 构建一个客户端实例
        client = new Client(ALIYUN_LOG_ENDPOINT, accessKeyId, accessKeySecret);
        return client;
    }


    @Override
    public TableVO<List<LogHistogramsVO>> getLogHistogramsPage(long logServiceId, int page, int length) {
        int size = logServiceDao.getLogHistogramsSize(logServiceId);
        List<LogHistogramsDO> list = logServiceDao.getLogHistogramsPage(logServiceId, page * length, length);
        List<LogHistogramsVO> voList = new ArrayList<>();
        for (LogHistogramsDO logHistogramsDO : list) {
            String gmtFrom = TimeUtils.stampSecToDate(String.valueOf(logHistogramsDO.getTimeFrom()));
            String gmtTo = TimeUtils.stampSecToDate(String.valueOf(logHistogramsDO.getTimeTo()));
            voList.add(new LogHistogramsVO(logHistogramsDO, gmtFrom, gmtTo));
        }
        return new TableVO<>(size, voList);
    }


    private LogServiceServerGroupCfgVO getLogServiceServerGroupCfg(ServerGroupDO serverGroupDO) {
        LogServiceServerGroupCfgDO logServiceServerGroupCfgDO = logServiceDao.queryLogServiceServerGroupCfg(serverGroupDO.getId());
        if (logServiceServerGroupCfgDO == null)
            return new LogServiceServerGroupCfgVO(serverGroupDO);
        KeyboxUserServerDO userServerDO = new KeyboxUserServerDO(SessionUtils.getUsername(), serverGroupDO.getId());
        boolean authed = false;
        long size = keyBoxService.getUserServerSize(userServerDO);
        if (size > 0)
            authed = true;
        return new LogServiceServerGroupCfgVO(serverGroupDO, logServiceServerGroupCfgDO, authed);
    }

    @Override
    public TableVO<List<LogServiceServerGroupCfgVO>> queryServerGroupPage(int page, int length, String name, boolean isUsername, int useType) {
        List<String> filterGroups = authService.getUserGroup(SessionUtils.getUsername());


        // List<ServerGroupDO> list = serverGroupDao.queryServerGroupPage(filterGroups, page * length, length, name, ServerGroupDO.UseTypeEnum.webservice.getCode());

        String username = (isUsername ? SessionUtils.getUsername() : "");
        long size = serverGroupDao.queryLogServiceServerGroupSize(name, username, useType);
        List<ServerGroupDO> list = serverGroupDao.queryLogServiceServerGroupPage(page * length, length, name, username, useType);


        List<LogServiceServerGroupCfgVO> voList = new ArrayList<>();
        for (ServerGroupDO serverGroupDO : list) {
            voList.add(getLogServiceServerGroupCfg(serverGroupDO));
        }

        return new TableVO<>(size, voList);

    }


}
