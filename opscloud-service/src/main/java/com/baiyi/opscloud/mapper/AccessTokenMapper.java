package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.AccessToken;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface AccessTokenMapper extends Mapper<AccessToken> {

    /**
     * 判断用户是否可访问某个资源
     * @param accessToken
     * @param resourceName
     * @return
     */
    int checkUserHasResourceAuthorize(@Param("accessToken") String accessToken, @Param("resourceName") String resourceName);
}