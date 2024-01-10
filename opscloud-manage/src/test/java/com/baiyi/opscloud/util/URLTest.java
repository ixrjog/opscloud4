package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/10/31 11:12
 * @Version 1.0
 */
public class URLTest extends BaseUnit {

    // https://static-legacy.dingtalk.com/media/lQDPDhvVtvtbMfPNA8LNA8CwHt7eHxEhYoQDLtLPsEC3AA_960_962.jpg

    @Test
    void parseURL() {
        try {
            URL url = URI.create("https://static-legacy.dingtalk.com/media/lQDPDhvVtvtbMfPNA8LNA8CwHt7eHxEhYoQDLtLPsEC3AA_960_962.jpg").toURL();
            print("host=" + url.getHost());
            print("path=" + url.getPath());
            print("query=" + url.getQuery());
            print("file=" + url.getFile());
            print("protocol=" + url.getProtocol());
            print(url);
        } catch (MalformedURLException e) {
            print(e.getMessage());
        }
    }

    @Test
    void dateTest() {
        Date doomsday = NewTimeUtil.parse("2023-02-20 22:22:22");
        print(doomsday.after(new Date()));
        print(doomsday.before(new Date()));
        /*
          true
          false
         */
    }

}