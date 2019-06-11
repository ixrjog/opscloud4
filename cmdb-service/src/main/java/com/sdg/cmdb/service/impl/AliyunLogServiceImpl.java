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
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.logService.*;
import com.sdg.cmdb.domain.keybox.KeyboxUserServerDO;
import com.sdg.cmdb.domain.logService.logServiceQuery.*;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerGroupUseTypeDO;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.BeanCopierUtils;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.TimeUtils;
import com.sdg.cmdb.util.TimeViewUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    private LogServiceDao logServiceDao;

    @Resource
    private ServerDao serverDao;

    @Resource
    private UserDao userDao;

    @Resource
    private KeyBoxService keyBoxService;

    @Autowired
    private ServerGroupService serverGroupService;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Value(value = "${aliyun.access.key}")
    private String accessKey;

    @Value(value = "${aliyun.access.secret}")
    private String accessSecret;

    static private Client client;

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;


    @Override
    public TableVO<List<LogServiceCfgDO>> getLogServiceCfgPage(int page, int length, String serverName) {
        long size = 0;
        List<LogServiceCfgDO> list = logServiceDao.queryLogServiceCfgPage(page * length, length, serverName);
        return new TableVO<>(size, list);
    }


    @Override
    public LogServiceVO queryLog(LogserviceQueryAbs logserviceQuery) throws LogException {

        try {
            long queryFrom = TimeUtils.dateToStamp(logserviceQuery.acqQueryBeginDate());
            int from = (int) (queryFrom / 1000);
            long queryTo = TimeUtils.dateToStamp(logserviceQuery.acqQueryEndDate());
            int to = (int) (queryTo / 1000);
            LogServiceQueryCfg cfg = logserviceQuery.acqLogServiceQueryCfg();
            String query = acqQuery(logserviceQuery);
            if (from > to) return new LogServiceVO();
            String project = cfg.acqProject();
            String logstore = cfg.acqLogstore();
            String topic = cfg.acqTopic();
            GetHistogramsResponse res = queryHistograms(project, logstore, topic, from, to, query);
            UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
            LogServiceDO logServiceDO = new LogServiceDO(cfg, userDO, query, from, to);
            logServiceDO.setTotalCount((int) res.GetTotalCount());
            logServiceDao.addLogService(logServiceDO);
            long logServiceId = logServiceDO.getId();
            int histogramsCnt = 0;
            for (Histogram ht : res.GetHistograms()) {
                if (ht.GetCount() == 0l) continue;
                LogHistogramsDO logHistogramsDO = new LogHistogramsDO(ht, logServiceId);
                logServiceDao.addLogHistograms(logHistogramsDO);
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
            invokeLogServicePath(logServiceVO, logserviceQuery);
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


    private void invokeLogServicePath(LogServiceVO logServiceVO, LogserviceQueryAbs logServiceQuery) {
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


    private String acqQuery(LogserviceQueryAbs query) {
        switch (query.acqQueryType()) {
            case 0:
                return acqQuery((LogserviceNginxQuery) query);
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

    /**
     * private String request_time;
     * private String http_x_forwarded_for;
     * private String upstream_addr;
     * private String uri;
     * private String upstream_response_time;
     * private String status;
     *
     * @param q
     * @return
     */
    private String acqQuery(LogserviceNginxQuery q) {
        String query = "";
        if (!StringUtils.isEmpty(q.getLogServiceCfg().getLogPath())) {
            query += LogServiceDefaultQuery.TAG_PATH_KEY + ":" + q.getLogServiceCfg().getLogPath();
        }

        if (!StringUtils.isEmpty(q.getHttp_x_forwarded_for())) {
            query += (StringUtils.isEmpty(query)) ? "" : " and ";
            query += "http_x_forwarded_for = " + q.getHttp_x_forwarded_for();
        }
        if (!StringUtils.isEmpty(q.getUpstream_addr())) {
            query += (StringUtils.isEmpty(query)) ? "" : " and ";
            query += "upstream_addr = " + q.getUpstream_addr();
        }
        if (!StringUtils.isEmpty(q.getUpstream_response_time())) {
            query += (StringUtils.isEmpty(query)) ? "" : " and ";
            query += "upstream_response_time = " + q.getUpstream_response_time();
        }
        if (!StringUtils.isEmpty(q.getStatus())) {
            query += (StringUtils.isEmpty(query)) ? "" : " and ";
            query += "status = " + q.getStatus();
        }
        if (!StringUtils.isEmpty(q.getUri())) {
            query += (StringUtils.isEmpty(query)) ? "" : " and ";
            query += "uri = " + q.getUri();
        }
        if (!StringUtils.isEmpty(q.getRequest_time())) {
            query += (StringUtils.isEmpty(query)) ? "" : " and ";
            query += "request_time >= " + q.getRequest_time();
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
            }

            list.add(lf);

        }
        invokePath(map);
        return new TableVO<>(list.size(), list);
    }


    @Override
    public TableVO<List<LogFormatNginx>> queryWwwLog(LogHistogramsVO logHistogramsVO) throws LogException {
        LogServiceDO logServiceDO = logServiceDao.queryLogServiceById(logHistogramsVO.getLogServiceId());
        ArrayList<QueriedLog> logs = queryLog(logHistogramsVO, logServiceDO);
        List<LogFormatNginx> list = new ArrayList<>();
        // TODO 缓存服务器信息
        HashMap<String, ServerDO> serverMap = new HashMap<String, ServerDO>();
        for (QueriedLog queriedLog : logs) {
            JSONObject jsStr = JSONObject.parseObject(queriedLog.GetLogItem().ToJsonString());
            LogFormatNginx lf = (LogFormatNginx) JSONObject.toJavaObject(jsStr, LogFormatNginx.class);
            if (!StringUtils.isEmpty(lf.getUpstream_addr())) {
                String ip = lf.getUpstream_addr().split(":")[0];
                if (!serverMap.containsKey(ip))
                    serverMap.put(ip, serverDao.queryServerByInsideIp(ip));
                lf.setServerDO(serverMap.get(ip));
            }
            lf.setSource(jsStr.getString("__source__"));
            lf.setHostname(jsStr.getString("__tag__:__hostname__"));
            lf.setPath(jsStr.getString("__tag__:__path__"));
            lf.setReceive_time(jsStr.getString("__tag__:__receive_time__"));
            lf.setTopic(jsStr.getString("__topic__"));
            try {
                lf.setGmtLogtime(TimeUtils.stampSecToDate(String.valueOf(queriedLog.mLogItem.GetTime())));
            } catch (Exception e) {

            }
            list.add(lf);
        }
        return new TableVO<>(list.size(), list);
    }

    @Override
    public TableVO<List<LogFormatNginx>> queryNginxLog(LogHistogramsVO logHistogramsVO) throws LogException {
        LogServiceDO logServiceDO = logServiceDao.queryLogServiceById(logHistogramsVO.getLogServiceId());
        if (logServiceDO.getLogstore().equalsIgnoreCase("default")) {
            return queryWwwLog(logHistogramsVO);
        }
        return null;
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
        // 构建一个客户端实例
        client = new Client(ALIYUN_LOG_ENDPOINT, accessKey, accessSecret);
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

        ServerGroupUseTypeDO useType = serverGroupService.getUseType(serverGroupDO.getUseType());

        if (logServiceServerGroupCfgDO == null)
            return new LogServiceServerGroupCfgVO(serverGroupDO, useType);
        KeyboxUserServerDO userServerDO = new KeyboxUserServerDO(SessionUtils.getUsername(), serverGroupDO.getId());
        boolean authed = false;
        long size = keyBoxService.getUserServerSize(userServerDO);
        if (size > 0)
            authed = true;
        return new LogServiceServerGroupCfgVO(serverGroupDO, logServiceServerGroupCfgDO, authed, useType);
    }

    @Override
    public TableVO<List<LogServiceServerGroupCfgVO>> queryServerGroupPage(int page, int length, String name, boolean isUsername, int useType) {
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
