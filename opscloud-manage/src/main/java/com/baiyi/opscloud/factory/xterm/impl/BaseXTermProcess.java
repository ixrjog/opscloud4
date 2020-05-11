package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.facade.KeyboxFacade;
import com.baiyi.opscloud.facade.OcAuthFacade;
import com.baiyi.opscloud.facade.ServerGroupFacade;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.factory.xterm.XTermProcessFactory;
import com.baiyi.opscloud.service.user.OcUserService;
import com.baiyi.opscloud.xterm.message.BaseXTermWSMessage;
import com.baiyi.opscloud.xterm.model.JSchSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:35 上午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseXTermProcess implements IXTermProcess, InitializingBean {

    @Resource
    protected OcAuthFacade ocAuthFacade;

    @Resource
    protected OcUserService ocUserService;

    @Resource
    protected ServerGroupFacade serverGroupFacade;

    @Resource
    protected KeyboxFacade keyboxFacade;

    protected Map<String, Map<String, JSchSession>> connectionSessionPool = new ConcurrentHashMap<>();

  //  protected static final String sessionId = UUID.randomUUID().toString();

    abstract protected BaseXTermWSMessage getXTermMessage(String message);

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        XTermProcessFactory.register(this);
    }

}
