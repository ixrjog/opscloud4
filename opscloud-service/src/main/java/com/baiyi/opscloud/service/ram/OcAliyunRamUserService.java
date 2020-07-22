package com.baiyi.opscloud.service.ram;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;
import com.baiyi.opscloud.domain.param.cloud.AliyunRAMUserParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/9 1:48 下午
 * @Version 1.0
 */
public interface OcAliyunRamUserService {

    List<OcAliyunRamUser> queryOcAliyunRamUserByAccountUid(String accountUid);

    OcAliyunRamUser queryOcAliyunRamUserByUniqueKey(String accountUid, String ramUsername);

    void addOcAliyunRamUser(OcAliyunRamUser ocAliyunRamUser);

    void updateOcAliyunRamUser(OcAliyunRamUser ocAliyunRamUser);

    void deleteOcAliyunRamUserById(int id);

    DataTable<OcAliyunRamUser> queryOcAliyunRamUserByParam(AliyunRAMUserParam.RamUserPageQuery pageQuery);

    List<OcAliyunRamUser> queryUserPermissionRamUserByUserId(int userId);

}
