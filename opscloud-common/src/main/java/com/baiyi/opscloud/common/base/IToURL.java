package com.baiyi.opscloud.common.base;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * @Author baiyi
 * @Date 2023/12/25 13:49
 * @Version 1.0
 */
public abstract class IToURL {

    public URL toURL() throws MalformedURLException {
        return URI.create(acqURL()).toURL();
    }

    protected abstract String acqURL();

}