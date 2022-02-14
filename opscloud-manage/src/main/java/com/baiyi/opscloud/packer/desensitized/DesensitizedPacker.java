package com.baiyi.opscloud.packer.desensitized;

import com.baiyi.opscloud.domain.annotation.DesensitizedMethod;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2021/6/11 2:18 下午
 * @Since 1.0
 */

@Component
public class DesensitizedPacker<T> {

    @DesensitizedMethod
    public void desensitized(T source) {
    }
}
