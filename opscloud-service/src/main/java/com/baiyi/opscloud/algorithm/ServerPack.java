package com.baiyi.opscloud.algorithm;

import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/8/26 9:46 上午
 * @Version 1.0
 */
@Data
@Builder
public class ServerPack implements Serializable {

    @Serial
    private static final long serialVersionUID = 6584215052593066163L;
    private Server server;
    private ServerProperty.Server property;
    private Env env;

}