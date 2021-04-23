package com.baiyi.opscloud.service.menu;

import com.baiyi.opscloud.domain.generator.opscloud.OcSubmenu;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/29 4:51 下午
 * @Since 1.0
 */
public interface OcSubmenuService {

    void addOcSubmenu(OcSubmenu ocSubmenu);

    void updateOcSubmenu(OcSubmenu ocSubmenu);

    void delOcSubmenu(int id);

    OcSubmenu queryOcSubmenu(int id);

    List<OcSubmenu> queryOcSubmenuByMenuId(Integer menuId);

    List<OcSubmenu> queryOcSubmenuByIdList(List<Integer> idList);

}
