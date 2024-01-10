package com.baiyi.opscloud.domain.vo.business;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.vo.user.UserVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/13 10:22 上午
 * @Version 1.0
 */
public interface IBusinessPermissionUser extends BaseBusiness.IBusiness {

    void setUsers(List<UserVO.User> users);

}