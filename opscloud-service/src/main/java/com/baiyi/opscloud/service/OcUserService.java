package com.baiyi.opscloud.service;

import com.baiyi.opscloud.domain.generator.OcUser;

/**
 * @Author baiyi
 * @Date 2020/1/3 6:51 下午
 * @Version 1.0
 */
public interface OcUserService {

    void addOcUser(OcUser ocUser);

    void updateOcUser(OcUser ocUser);

    OcUser queryOcUserById(Integer id);

    OcUser queryOcUserByUsername(String username);

    void delOcUserByUsername(String username);


}
