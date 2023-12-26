package com.baiyi.opscloud.tencent.exmail.feign;

import com.baiyi.opscloud.tencent.exmail.entity.ExmailUser;
import com.baiyi.opscloud.tencent.exmail.entity.base.BaseExmailResult;
import com.baiyi.opscloud.tencent.exmail.param.ExmailParam;
import feign.Param;
import feign.RequestLine;

import java.util.List;

/**
 * Doc:
 * https://exmail.qq.com/qy_mng_logic/doc#10017
 * @Author baiyi
 * @Date 2021/10/14 5:04 下午
 * @Version 1.0
 */
public interface TencentExmailUserFeign {

    @RequestLine("POST /cgi-bin/user/create?access_token={token}")
    BaseExmailResult createUser(@Param("token") String token, ExmailParam.User user);

    @RequestLine("GET /cgi-bin/user/get?access_token={token}&userid={userId}")
    ExmailUser getUser(@Param("token") String token, @Param("userId") String userId);

    @RequestLine("GET /cgi-bin/user/list?access_token={token}&department_id={departmentId}&fetch_child=1")
    List<ExmailUser> listUser(@Param("token") String token, @Param("departmentId") Long departmentId);

    @RequestLine("POST /cgi-bin/user/update?access_token={token}")
    BaseExmailResult updateUser(@Param("token") String token, ExmailParam.User user);

    @RequestLine("GET /cgi-bin/user/delete?access_token={token}&userid={userId}")
    BaseExmailResult deleteUser(@Param("token") String token, @Param("userId") String userId);

}