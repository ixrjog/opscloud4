package com.baiyi.opscloud.datasource.aliyun.log.driver;

import com.baiyi.opscloud.datasource.aliyun.log.driver.base.AbstractAliyunLogDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/6/13 9:46 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunLogDriver extends AbstractAliyunLogDriver {

    /**
     * 列出 project
     *
     * @param aliyun
     * @param project
     * @return
     */
//    public List<Project> listProject(AliyunConfig.Aliyun aliyun, String project) {
//        int offset = 0;
//        ListProjectRequest req = new ListProjectRequest(project, offset, QUERY_SIZE);
//        try {
//            Client client = buildClient(aliyun);
//            return client.ListProject(req).getProjects();
//        } catch (LogException lg) {
//            log.error("阿里云日志服务查询Project错误: {}", lg.GetErrorMessage());
//        }
//        return Collections.emptyList();
//    }

    /**
     * 列出当前 project 下的所有日志库名称
     *
     * @param aliyun
     * @param project
     * @return
     */
//    public List<String> listLogstore(AliyunConfig.Aliyun aliyun, String project) {
//        int offset = 0;
//        String logStoreSubName = "";
//        ListLogStoresRequest req = new ListLogStoresRequest(project, offset, QUERY_SIZE, logStoreSubName);
//        try {
//            Client client = buildClient(aliyun);
//            return client.ListLogStores(req).GetLogStores();
//        } catch (LogException lg) {
//            log.error("阿里云日志服务查询LogStore错误: {}", lg.GetErrorMessage());
//        }
//        return Collections.emptyList();
//    }

//    public List<String> listConfig(AliyunConfig.Aliyun aliyun, String project, String logstore) {
//        int offset = 0;
//        ListConfigRequest req = new ListConfigRequest(project, offset, QUERY_SIZE);
//        req.SetLogstoreName(logstore);
//        try {
//            Client client = buildClient(aliyun);
//            return client.ListConfig(req).GetConfigs();
//        } catch (LogException lg) {
//            log.error("阿里云日志服务查询Config错误: {}", lg.GetErrorMessage());
//        }
//        return Collections.emptyList();
//    }

}
