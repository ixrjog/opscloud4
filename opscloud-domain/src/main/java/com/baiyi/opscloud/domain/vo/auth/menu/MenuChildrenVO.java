package com.baiyi.opscloud.domain.vo.auth.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/23 9:05 上午
 * @Version 1.0
 */
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuChildrenVO {

    private String path;
    private String title;
    private String icon;
    private String iconSvg;
}
