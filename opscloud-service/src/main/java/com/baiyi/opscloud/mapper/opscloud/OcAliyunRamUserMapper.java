package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;
import com.baiyi.opscloud.domain.param.cloud.AliyunRAMUserParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunRamUserMapper extends Mapper<OcAliyunRamUser> {

    List<OcAliyunRamUser> queryOcAliyunRamUserByParam(AliyunRAMUserParam.RamUserPageQuery pageQuery);

    List<OcAliyunRamUser> queryUserPermissionRamUserByUserId(int userId);
}