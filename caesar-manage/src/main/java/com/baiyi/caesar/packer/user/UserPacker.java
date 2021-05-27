package com.baiyi.caesar.packer.user;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.packer.auth.AuthRolePacker;
import com.baiyi.caesar.util.ExtendUtil;
import com.baiyi.caesar.vo.user.UserVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:49 上午
 * @Version 1.0
 */
@Component
public class UserPacker {

    @Resource
    private AuthRolePacker authRolePacker;

    public List<UserVO.User> wrapVOList(List<User> data) {
        return BeanCopierUtil.copyListProperties(data, UserVO.User.class);
    }

    public List<UserVO.User> wrapVOList(List<User> data, IExtend iExtend) {
        List<UserVO.User> voList = wrapVOList(data);
        if (!ExtendUtil.isExtend(iExtend))
            return voList;
        return voList.stream().peek(e -> authRolePacker.wrap(e)).collect(Collectors.toList());
    }


}
