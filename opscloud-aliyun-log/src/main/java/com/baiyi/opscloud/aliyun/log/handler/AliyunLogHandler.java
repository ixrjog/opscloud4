package com.baiyi.opscloud.aliyun.log.handler;

import com.aliyun.openservices.log.common.Project;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.ListConfigRequest;
import com.aliyun.openservices.log.request.ListLogStoresRequest;
import com.aliyun.openservices.log.request.ListProjectRequest;
import com.baiyi.opscloud.aliyun.log.base.BaseAliyunLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/13 9:46 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunLogHandler extends BaseAliyunLog {

    public List<Project> getProjects(String project) {
        // 列出当前 project 下的所有日志库名称
        int offset = 0;
        ListProjectRequest req = new ListProjectRequest(project, offset, QUERY_SIZE);
        try {
            return client.ListProject(req).getProjects();
        } catch (LogException lg) {
            log.error("阿里云日志服务查询Project错误! , {}", lg.GetErrorMessage());
        }
        return Collections.emptyList();
    }

    public List<String> getLogStores(String project) {
        int offset = 0;
        String logStoreSubName = "";
        ListLogStoresRequest req = new ListLogStoresRequest(project, offset, QUERY_SIZE, logStoreSubName);
        try {
            return client.ListLogStores(req).GetLogStores();
        } catch (LogException lg) {
            log.error("阿里云日志服务查询LogStores错误! , {}", lg.GetErrorMessage());
        }
        return Collections.emptyList();
    }

    public List<String> getConfigs(String project, String logstore) {
        int offset = 0;
        ListConfigRequest req = new ListConfigRequest(project, offset, QUERY_SIZE);
        req.SetLogstoreName(logstore);

        try {
            return client.ListConfig(req).GetConfigs();
        } catch (LogException lg) {
        }
        return Collections.EMPTY_LIST;
    }

}
