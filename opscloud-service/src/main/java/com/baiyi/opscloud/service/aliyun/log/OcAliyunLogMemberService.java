package com.baiyi.opscloud.service.aliyun.log;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunLogMember;
import com.baiyi.opscloud.domain.param.cloud.AliyunLogMemberParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/15 9:23 上午
 * @Version 1.0
 */
public interface OcAliyunLogMemberService {

    DataTable<OcAliyunLogMember> queryOcAliyunLogMemberByParam(AliyunLogMemberParam.LogMemberPageQuery pageQuery);

    List<OcAliyunLogMember> queryOcAliyunLogMemberByLogId(int logId);

    List<OcAliyunLogMember> queryOcAliyunLogMemberByServerGroupId(int serverGroupId);

    int countOcAliyunLogMemberByLogId(int logId);

    void addOcAliyunLogMember(OcAliyunLogMember ocAliyunLogMember);

    void updateOcAliyunLogMember(OcAliyunLogMember ocAliyunLogMember);

    void deleteOcAliyunLogMemberById(int id);

    OcAliyunLogMember queryOcAliyunLogMemberByUniqueKey(OcAliyunLogMember ocAliyunLogMember);

    OcAliyunLogMember queryOcAliyunLogMemberById(int id);
}
