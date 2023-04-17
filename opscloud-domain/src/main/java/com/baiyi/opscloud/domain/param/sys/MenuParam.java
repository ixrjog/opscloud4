package com.baiyi.opscloud.domain.param.sys;

import com.baiyi.opscloud.domain.vo.sys.MenuVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 修远
 * @Date 2021/6/2 10:25 上午
 * @Since 1.0
 */
public class MenuParam {

    @Data
    @Schema
    public static class MenuSave {
        @NotEmpty(message = "菜单列表不能为空")
        @Schema(description = "菜单列表")
        private List<MenuVO.Menu> menuList;
    }

    @Data
    @Schema
    public static class MenuChildSave {
        @Schema(description = "子菜单列表")
        private List<MenuVO.Child> menuChildList;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class AuthRoleMenuSave {

        @Schema(description = "角色ID")
        private Integer roleId;

        @NotEmpty(message = "菜单列表不能为空")
        @Schema(description = "菜单ID列表")
        private List<Integer> menuChildIdList;

    }

}