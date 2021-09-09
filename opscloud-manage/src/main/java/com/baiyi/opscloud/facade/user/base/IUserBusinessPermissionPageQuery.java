package com.baiyi.opscloud.facade.user.base;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.vo.user.UserVO;

/**
 * @Author baiyi
 * @Date 2021/6/17 9:20 上午
 * @Version 1.0
 */
public interface IUserBusinessPermissionPageQuery {

    DataTable<UserVO.IUserPermission> queryUserBusinessPermissionPage(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery);

    Integer getBusinessType();
}
