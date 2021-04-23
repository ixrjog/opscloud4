package com.baiyi.opscloud.domain.param.menu;

import com.baiyi.opscloud.domain.generator.opscloud.OcMenu;
import com.baiyi.opscloud.domain.generator.opscloud.OcSubmenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/29 5:30 下午
 * @Since 1.0
 */
public class MenuParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class MenuSave {

        @NotEmpty(message = "菜单列表不能为空")
        @ApiModelProperty(value = "菜单列表")
        private List<OcMenu> menuList;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class SubmenuSave {

        @ApiModelProperty(value = "子菜单列表")
        private List<OcSubmenu> submenuList;
    }

    @Data
    @Builder
    public static class TempBuild {

        private List<menu> menuList;
    }

    @Data
    @Builder
    public static class menu {

        private String menuTitle;
        private String menuIcon;
        private Integer menuOrder;
        private List<OcSubmenu> submenuList;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class RoleMenuSave {

        private Integer roleId;

        @NotEmpty(message = "菜单列表不能为空")
        private List<Integer> submenuIdList;
    }
}
