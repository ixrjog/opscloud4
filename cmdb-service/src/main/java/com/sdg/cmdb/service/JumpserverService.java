package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.jumpserver.AssetsAdminuserDO;
import com.sdg.cmdb.domain.jumpserver.AssetsSystemuserDO;
import com.sdg.cmdb.domain.jumpserver.JumpserverVO;

import java.util.List;

public interface  JumpserverService {

    BusinessWrapper<Boolean> syncAssets();

    BusinessWrapper<Boolean>  syncUsers();

    List<AssetsSystemuserDO> queryAssetsSystemuser(String name);

    List<AssetsAdminuserDO> queryAssetsAdminuser(String name);

    BusinessWrapper<Boolean> saveAssetsSystemuser(String id);

    BusinessWrapper<Boolean> saveAssetsAdminuser(String id);

    JumpserverVO getJumpserver();
}
