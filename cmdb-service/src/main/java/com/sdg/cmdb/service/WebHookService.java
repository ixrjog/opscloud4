package com.sdg.cmdb.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebHookService {

   // public static final String PICC_ADDRESS = "http://test.mypicc.com.cn";
    public static HttpClientContext httpClientContext = null;

    static {
        httpClientContext = HttpClientContext.create();  //创建上下文.用于共享sessionid
    }

    public static HttpResponse post(String url, Object object) {
        // URI uri = new URI(url);
        Gson gson = new GsonBuilder().create();
        String postBody = gson.toJson(object);

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity se = new StringEntity(postBody, "utf-8");

        httppost.setEntity(se);
        try {
            HttpResponse response = httpclient.execute(httppost);
            String result = EntityUtils.toString(response.getEntity());
            //if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static HttpResponse post(Map<String, String> map, String url) {
        HttpClient client = (HttpClient) HttpClients.createDefault(); //获取链接对象.
        HttpPost post = new HttpPost(url); //创建表单.
        ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();//用于存放表单数据.

        //遍历map 将其中的数据转化为表单数据
        for (Map.Entry<String, String> entry :
                map.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        try {
            //对表单数据进行url编码
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs);
            post.setEntity(urlEncodedFormEntity);

            HttpResponse response = client.execute(post, httpClientContext);//发送数据.提交表单
            CookieStore cookieStore = httpClientContext.getCookieStore(); //获取cookie 第一步
            List<Cookie> cookies = cookieStore.getCookies();  //获取所有的cookie
            //System.out.println("gyqtest---------cookies.size" + cookies.size());
//            for (Cookie cookie :
//                    cookies) {
//                System.out.println("gyqtest---------name=" + cookie.getName() + "====value=" + cookie.getValue());
//            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}