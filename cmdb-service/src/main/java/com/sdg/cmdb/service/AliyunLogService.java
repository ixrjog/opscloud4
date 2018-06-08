package com.sdg.cmdb.service;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.exception.LogException;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.logService.*;
import com.sdg.cmdb.domain.logService.logServiceQuery.*;

import java.util.List;

/**
 * Created by liangjian on 2017/9/18.
 */
public interface AliyunLogService {

    /**
     * @param page
     * @param length
     * @param serverName
     * @return
     */
    TableVO<List<LogServiceCfgDO>> getLogServiceCfgPage(int page, int length, String serverName);

    /**
     * JAVA日志初始化视图
     * @param logServiceQuery
     * @return
     * @throws LogException
     */
    LogServiceVO queryLog(LogServiceQuery logServiceQuery) throws LogException;


    TableVO<List<LogHistogramsVO>> getLogHistogramsPage(long logServiceId, int page, int length);

    Object queryNginxLog(LogHistogramsVO logHistogramsVO) throws LogException;

    TableVO<List<LogFormatKa>> queryKaLog(LogHistogramsVO logHistogramsVO) throws LogException;

    TableVO<List<LogFormatWww>> queryWwwLog(LogHistogramsVO logHistogramsVO) throws LogException;

    //查询日志详情
    TableVO<List<LogFormatDefault>> queryDefaultLog(LogHistogramsVO logHistogramsVO) throws LogException;


    TableVO<List<LogServicePathDO>> getLogServicePathPage(int page, int length, String tagPath, long serverGroupId);


    TableVO<List<LogServiceServerGroupCfgVO>> queryServerGroupPage(int page, int length, String name,boolean isUsername,int useType);


    Client acqClient();


}
