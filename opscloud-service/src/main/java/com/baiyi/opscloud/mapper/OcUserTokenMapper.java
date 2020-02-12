package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.OcUserToken;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface OcUserTokenMapper extends Mapper<OcUserToken> {

    /**
     * 判断用户是否可访问某个资源
     * @param token
     * @param resourceName
     * @return
     */
    int checkUserHasResourceAuthorize(@Param("token") String token, @Param("resourceName") String resourceName);

}