package com.baiyi.opscloud.domain.vo.auth.menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/23 9:04 上午
 * @Version 1.0
 */
@Builder
@Data
@JsonIgnoreProperties
public class MenuVO {

    private String title;
    private String icon;
    private List<MenuChildrenVO> children;
}
