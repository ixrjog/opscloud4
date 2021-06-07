package com.baiyi.caesar.domain.param.sys;

import com.baiyi.caesar.domain.vo.sys.MenuVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/2 10:25 上午
 * @Since 1.0
 */
public class MenuParam {

    @Data
    @ApiModel
    public static class MenuSave {
        @NotEmpty(message = "菜单列表不能为空")
        @ApiModelProperty(value = "菜单列表")
        private List<MenuVO.Menu> menuList;
    }

    @Data
    @ApiModel
    public static class MenuChildSave {
        @ApiModelProperty(value = "子菜单列表")
        private List<MenuVO.Child> menuChildList;
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AuthRoleMenuSave {

        @ApiModelProperty(value = "角色id")
        private Integer roleId;

        @NotEmpty(message = "菜单列表不能为空")
        @ApiModelProperty(value = "菜单id列表")
        private List<Integer> menuChildIdList;
    }
}

