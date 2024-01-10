package com.baiyi.opscloud.datasource.nacos.feign;

import com.baiyi.opscloud.datasource.nacos.entity.NacosLogin;
import com.baiyi.opscloud.datasource.nacos.entity.NacosPermission;
import com.baiyi.opscloud.datasource.nacos.entity.NacosRole;
import com.baiyi.opscloud.datasource.nacos.entity.NacosUser;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/11/11 4:17 下午
 * @Version 1.0
 */
public interface NacosAuthV1Feign {

    @RequestLine("POST /nacos/v1/auth/users/login")
    @Headers({"Content-type: application/x-www-form-urlencoded"})
    NacosLogin.AccessToken login(@QueryMap Map<String, String> loginParam);

    @RequestLine("GET /nacos/v1/auth/permissions?" +
            "pageNo={pageNo}" +
            "&pageSize={pageSize}" +
            "&accessToken={accessToken}")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    NacosPermission.PermissionsResponse listPermissions(@Param("pageNo") Integer pageNo,
                                                        @Param("pageSize") Integer pageSize,
                                                        @Param("accessToken") String accessToken);

    @RequestLine("POST /nacos/v1/auth/users?" +
            "accessToken={accessToken}")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    NacosUser.CreateUserResponse createUser(@Param("accessToken") String accessToken, @QueryMap Map<String, String> paramMap);

    @RequestLine("POST /nacos/v1/auth/roles?" +
            "accessToken={accessToken}")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    NacosUser.AuthRoleResponse authRole(@Param("accessToken") String accessToken, @QueryMap Map<String, String> paramMap);

    @RequestLine("GET /nacos/v1/auth/users/search?" +
            "username={username}" +
            "&accessToken={accessToken}")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    List<String> searchUsers(@Param("username") String username,
                             @Param("accessToken") String accessToken);

    @RequestLine("GET /nacos/v1/auth/users?" +
            "pageNo={pageNo}" +
            "&pageSize={pageSize}" +
            "&accessToken={accessToken}")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    NacosUser.UsersResponse listUsers(@Param("pageNo") Integer pageNo,
                        @Param("pageSize") Integer pageSize,
                        @Param("accessToken") String accessToken);

    @RequestLine("GET /nacos/v1/auth/roles?" +
            "pageNo={pageNo}" +
            "&pageSize={pageSize}" +
            "&accessToken={accessToken}")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    NacosRole.RolesResponse listRoles(@Param("pageNo") Integer pageNo,
                                      @Param("pageSize") Integer pageSize,
                                      @Param("accessToken") String accessToken);

}