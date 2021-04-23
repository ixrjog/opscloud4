package com.baiyi.opscloud.domain.vo.auth.menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/23 9:04 上午
 * @Version 1.0
 */
@Builder
@Data
@JsonIgnoreProperties
public class MenuVO implements Serializable {

    private static final long serialVersionUID = 3106433599365367715L;
    private String title;
    private String icon;
    private List<MenuChildrenVO> children;
}
