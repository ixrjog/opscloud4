package com.baiyi.opscloud.facade.aliyun;

import com.aliyun.openservices.log.common.Project;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.param.cloud.AliyunLogMemberParam;
import com.baiyi.opscloud.domain.param.cloud.AliyunLogParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunLogMemberVO;
import com.baiyi.opscloud.domain.vo.cloud.AliyunLogVO;

import java.util.List;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/6/13 11:54 上午
 * @Version 1.0
 */
public interface AliyunLogFacade {

    DataTable<AliyunLogVO.Log> queryLogPage(AliyunLogParam.AliyunLogPageQuery pageQuery);

    BusinessWrapper<Boolean> updateLog(AliyunLogVO.Log log);

    BusinessWrapper<Boolean> addLog(AliyunLogVO.Log log);

    List<Project> getProjects(AliyunLogParam.ProjectQuery query);

    List<String> getLogStores(AliyunLogParam.LogStoreQuery query);

    List<String> getConfigs(AliyunLogParam.ConfigQuery query);

    DataTable<AliyunLogMemberVO.LogMember> queryLogMemberPage(AliyunLogMemberParam.LogMemberPageQuery pageQuery);

    BusinessWrapper<Boolean> pushLogMember(int id);

    BusinessWrapper<Boolean> pushLog(int id);

    BusinessWrapper<Boolean> addLogMember(AliyunLogMemberParam.AddLogMember addLogMember);

    BusinessWrapper<Boolean> removeLogMember(int id);

    BusinessWrapper<List<OcServerGroup>> queryLogMemberServerGroupPage(ServerGroupParam.LogMemberServerGroupQuery pageQuery);

    void pushTask(Set<Integer> keySet);
}
