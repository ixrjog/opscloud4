package com.sdg.cmdb.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdg.cmdb.domain.jenkins.BaseParamDO;
import com.sdg.cmdb.domain.jenkins.webhook.HookAndroidDebugNote;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class WebHookServiceTest {




    @Test
    public void test() {
        BaseParamDO param = new BaseParamDO();
        param.setParamName("k");
        param.setParamValue("v");
        Gson gson = new GsonBuilder().create();
        System.err.println(gson.toJson(param));
    }

    @Test
    public void testPost() {
        HookAndroidDebugNote debugNote = new HookAndroidDebugNote();
        debugNote.setProject("app");
        debugNote.setApk("http://aaa.com/sdsdg/app.apk");

        //  boolean result = webHookServiceImpl.post("http://10.17.73.57:8080/webhookserver/executeCmd", debugNote);
        //  System.err.println(result);
    }


    @Test
    public void testPostFrom() {
//        HookAndroidDebugNote debugNote = new HookAndroidDebugNote();
//        debugNote.setProject("app");
//        debugNote.setApk("http://aaa.com/sdsdg/app.apk");

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("project","ka"));
        pairs.add(new BasicNameValuePair("apk","http://aaa.com/sdsdg/app.apk"));

//
//        boolean result = webHookServiceImpl.post("http://10.17.73.57:8080/webhookserver/executeCmd", pairs);
//        System.err.println(result);
        Map<String,String> map = new HashMap<>();
        map.put("project","ka");
        map.put("apk","http://aaa.com/sdsdg/app.apk");

        //WebHookServiceImpl.post(map,"http://10.17.73.57:8080/webhookserver/executeCmd");
    }

}
