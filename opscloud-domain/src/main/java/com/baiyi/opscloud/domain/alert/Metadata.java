package com.baiyi.opscloud.domain.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 修远
 * @Date 2022/7/26 4:31 PM
 * @Since 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

    private String name;
    private String ip;

}