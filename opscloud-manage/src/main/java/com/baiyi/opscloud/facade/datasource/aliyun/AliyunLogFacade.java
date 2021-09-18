package com.baiyi.opscloud.facade.datasource.aliyun;

import com.aliyun.openservices.log.common.Project;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.datasource.AliyunLogMemberParam;
import com.baiyi.opscloud.domain.param.datasource.AliyunLogParam;
import com.baiyi.opscloud.domain.vo.datasource.aliyun.AliyunLogMemberVO;
import com.baiyi.opscloud.domain.vo.datasource.aliyun.AliyunLogVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/16 5:31 下午
 * @Version 1.0
 */
public interface AliyunLogFacade {

    DataTable<AliyunLogVO.Log> queryLogPage(AliyunLogParam.AliyunLogPageQuery pageQuery);

    DataTable<AliyunLogMemberVO.LogMember> queryLogMemberPage(AliyunLogMemberParam.LogMemberPageQuery pageQuery);

    void updateLog(AliyunLogVO.Log log);

    void addLog(AliyunLogVO.Log log);

    void deleteLogById(Integer id);

    void pushLogMemberByServerGroupId(Integer id);

    void pushLogMemberById(Integer id);

    void pushLogById(Integer id);

    void addLogMember(AliyunLogMemberVO.LogMember logMember);

    void updateLogMember(AliyunLogMemberVO.LogMember logMember);

    void deleteLogMemberById(Integer id);

    List<Project> queryProject(AliyunLogParam.ProjectQuery query);

    List<String> queryLogstore(AliyunLogParam.LogStoreQuery query);

    List<String> queryConfig(AliyunLogParam.ConfigQuery query);
}
