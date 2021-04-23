package com.baiyi.opscloud.service.aliyun.ram;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamPermission;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/10 6:16 下午
 * @Version 1.0
 */
public interface OcAliyunRamPermissionService {

    void addOcAliyunRamPermission(OcAliyunRamPermission ocAliyunRamPermission);

    void deleteOcAliyunRamPermissionById(int id);

    OcAliyunRamPermission queryOcAliyunRamPermissionByUniqueKey(OcAliyunRamPermission ocAliyunRamPermission);

    List<OcAliyunRamPermission> queryOcAliyunRamPermissionByOcAliyunRamUser(OcAliyunRamUser ocAliyunRamUser);
}
