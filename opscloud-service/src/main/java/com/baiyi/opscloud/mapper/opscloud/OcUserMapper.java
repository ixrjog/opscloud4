package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.user.UserParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcUserMapper extends Mapper<OcUser> {

    OcUser queryByUsername(@Param("username") String username);

    List<OcUser> queryOcUserByParam(UserParam.UserPageQuery pageQuery);

    List<OcUser> fuzzyQueryUserByParam(UserParam.UserPageQuery pageQuery);

    int updateBaseUser(OcUser ocUser);

    List<OcUser> queryOcUserByUserGroupId(@Param("userGroupId")  int userGroupId);

    List<OcUser> queryOcUserByIdList(@Param("userIdList") List<Integer> userIdList);
}