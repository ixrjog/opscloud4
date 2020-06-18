package com.baiyi.opscloud.domain.vo.preview;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/20 2:03 下午
 * @Version 1.0
 */
@Data
@Builder
public class PreviewFileVO {

    private String name;
    private String content;
    private String path;
    @Builder.Default
    private String lang = "";
    private String comment;

}
