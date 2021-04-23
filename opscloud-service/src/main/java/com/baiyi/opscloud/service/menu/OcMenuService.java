package com.baiyi.opscloud.service.menu;

import com.baiyi.opscloud.domain.generator.opscloud.OcMenu;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/29 4:51 下午
 * @Since 1.0
 */
public interface OcMenuService {

    void addOcMenu(OcMenu ocMenu);

    void updateOcMenu(OcMenu ocMenu);

    void delOcMenu(int id);

    OcMenu queryOcMenu(int id);

    List<OcMenu> queryOcMenuAll();

}
