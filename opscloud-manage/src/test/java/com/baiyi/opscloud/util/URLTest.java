package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Author baiyi
 * @Date 2022/10/31 11:12
 * @Version 1.0
 */
public class URLTest extends BaseUnit {

    // https://static-legacy.dingtalk.com/media/lQDPDhvVtvtbMfPNA8LNA8CwHt7eHxEhYoQDLtLPsEC3AA_960_962.jpg

    @Test
    void paseURL() {
        try {
            URL url = new URL("https://static-legacy.dingtalk.com/media/lQDPDhvVtvtbMfPNA8LNA8CwHt7eHxEhYoQDLtLPsEC3AA_960_962.jpg");

            print("host=" + url.getHost());
            print("path=" + url.getPath());
            print("query=" + url.getQuery());
            print("file=" + url.getFile());
        } catch (MalformedURLException e) {
            print(e.getMessage());

        }


    }

}
