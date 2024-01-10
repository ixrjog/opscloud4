package com.baiyi.opscloud.service.datasource;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AliyunLogMember;
import com.baiyi.opscloud.domain.param.datasource.AliyunLogMemberParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/16 5:50 下午
 * @Version 1.0
 */
public interface AliyunLogMemberService {

    DataTable<AliyunLogMember> queryAliyunLogMemberByParam(AliyunLogMemberParam.LogMemberPageQuery pageQuery);

    List<AliyunLogMember> queryByServerGroupId(Integer serverGroupId);

    int countByAliyunLogId(Integer aliyunLogId);

    List<AliyunLogMember> queryByAliyunLogId(Integer aliyunLogId);

    void deleteById(Integer id);

    AliyunLogMember getById(Integer id);

    void update(AliyunLogMember aliyunLogMember);

    void add(AliyunLogMember aliyunLogMember);

}