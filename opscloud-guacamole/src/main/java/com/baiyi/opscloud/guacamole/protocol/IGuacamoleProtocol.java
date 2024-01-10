package com.baiyi.opscloud.guacamole.protocol;

import com.baiyi.opscloud.domain.param.guacamole.GuacamoleParam;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleSocket;

/**
 * @Author baiyi
 * @Date 2021/7/9 1:42 下午
 * @Version 1.0
 */
public interface IGuacamoleProtocol {

    String getProtocol();

    GuacamoleSocket buildGuacamoleSocket(GuacamoleParam.Login guacamoleLogin) throws GuacamoleException;

}