package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.generator.opscloud.OcUserToBeRetired;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/26 5:10 下午
 * @Since 1.0
 */
public interface OcUserToBeRetiredService {

    void addOcUserToBeRetired(OcUserToBeRetired ocUserToBeRetired);

    List<OcUserToBeRetired> queryOcUserToBeRetiredAll();

    void delOcUserToBeRetiredByUserId(Integer userId);
}
