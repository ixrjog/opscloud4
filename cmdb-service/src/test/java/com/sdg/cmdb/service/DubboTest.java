package com.sdg.cmdb.service;


import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.monitor.dubbo.DubboMonitor;
import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.dubbo.DubboProvider;
import com.sdg.cmdb.service.dubbo.QueryParamBO;
import com.sdg.cmdb.service.dubbo.TelnetConnection;
import com.sdg.cmdb.util.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.net.telnet.TelnetClient;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class DubboTest {

    private static List<String> getDubboInfo(String path) {
        List<String> list = new ArrayList<String>();
        // String servers = getProperties("zk.servers");
//        String roopath = getProperties("zk.rootpath");

        ZkClient zkClient = new ZkClient("47.99.2.51", 2181);


        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println("parentPath:" + s + ", currentchilds:" + list);
            }
        });

        try {
            list = zkClient.getChildren(path);
            Thread.sleep(1000);
//            return list;
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return list;
    }

    @Test
    public void test22(){


    }

    @Test
    public void test() {
        //  List<String> list = getDubboInfo("/dubbo/com.ggj.platform.promotion.api.coupon.CouponAPI/providers");
        /**
         * providers
         * consumers
         * configurators
         * routers
         *
         * dubbo/com.ggj.platform.promotion.api.coupon
         *
         * com.ggj.platform.promotion.api.coupon.CouponAPI/providers
         */
        List<String> list = getDubboInfo("/dubbo");
        for (String d : list)
            System.err.println(d);
    }

    @Test
    public void test222(){
        String s="dubbo%3A%2F%2F172.20.22.25%3A20880%2Fcom.ggj.platform.promotion.api.coupon.CouponAPI%3Fanyhost%3Dtrue%26application%3Dpromotion-platform%26default.delay%3D-1%26default.gsf.version%3D2.0.1-SNAPSHOT%26default.retries%3D0%26default.threads%3D600%26default.timeout%3D5000%26delay%3D-1%26dubbo%3D2.6.2%26generic%3Dfalse%26interface%3Dcom.ggj.platform.promotion.api.coupon.CouponAPI%26methods%3DgetCouponById%2ClistCouponRequest%2CcreateInvestmentCoupon%2CbatchSendCoupon%2CgetCouponByActivityIds%2CsendCoupons%2CreceiveCouponPackage%2CreceiveCoupon%2CsendBizCoupons%2ClistCouponByIds%2CinvalidInvestmentCoupon%26owner%3Dbujiu%26pid%3D1%26side%3Dprovider%26timeout%3D5000%26timestamp%3D1558947280568";

        DubboProvider dp = new DubboProvider(s);
        System.err.println(dp);
    }

    @Test
    public void test223(){
        String s="dubbo%3A%2F%2F172.20.22.25%3A20880%2Fcom.ggj.platform.promotion.api.coupon.CouponAPI%3Fanyhost%3Dtrue%26application%3Dpromotion-platform%26default.delay%3D-1%26default.gsf.version%3D2.0.1-SNAPSHOT%26default.retries%3D0%26default.threads%3D600%26default.timeout%3D5000%26delay%3D-1%26dubbo%3D2.6.2%26generic%3Dfalse%26interface%3Dcom.ggj.platform.promotion.api.coupon.CouponAPI%26methods%3DgetCouponById%2ClistCouponRequest%2CcreateInvestmentCoupon%2CbatchSendCoupon%2CgetCouponByActivityIds%2CsendCoupons%2CreceiveCouponPackage%2CreceiveCoupon%2CsendBizCoupons%2ClistCouponByIds%2CinvalidInvestmentCoupon%26owner%3Dbujiu%26pid%3D1%26side%3Dprovider%26timeout%3D5000%26timestamp%3D1558947280568";
        try {
            String url =URLDecoder.decode(s);
            url = url.replace("dubbo://","http://");
            URL u = new URL(url);
//            System.err.println(url);
//            String host = RegexUtils.getHost(url);
//            System.err.println(host);

            //URL url = new URL(URLDecoder.decode(s));
            System.err.println(JSON.toJSONString(u));
        }catch (Exception e){
               e.printStackTrace();
        }

    }


    @Test
    public void test2() {
        final List<Map<String, String>> LIST2 = new ArrayList<Map<String, String>>();
        final String ROOTPATH = "/dubbo";
        final List<String> LIST = getDubboInfo(ROOTPATH);

        for (int i = 0; i < LIST.size(); i++) {
            final int J = i;

            try {
                String path = ROOTPATH + "/" + LIST.get(J) + "/providers";
                List<String> list1 = getDubboInfo(path);
                if (list1.size() == 0) {
                } else {
                    String url = URLDecoder.decode(list1.get(0));
                    //检测URl是否为用户所需

                    System.out.println(url);
                    String host = "dubbo://" + url.split("\\?")[0].split("dubbo://")[1].split("/")[0];
                    String inter = url.split("\\?")[0].split("dubbo://")[1].split("/")[1];
                    //如果接口服务有端口号，需要带上
                    Map<String, String> map = new HashMap<String, String>();
                    if (url.contains("&version=")) {
                        String version = url.split("&version=")[1];
                        map.put("version", version);
                    }
                    map.put(host, inter);
                    LIST2.add(map);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test3() {

        try {
            TelnetClient telnetClient = new TelnetClient("vt200");  //指明Telnet终端类型，否则会返回来的数据中文会乱码
            telnetClient.setDefaultTimeout(5000); //socket延迟时间：5000ms
            telnetClient.connect("47.99.131.239", 10008);  //建立一个连接,默认端口是23
            InputStream inputStream = telnetClient.getInputStream(); //读取命令的流
            PrintStream pStream = new PrintStream(telnetClient.getOutputStream());  //写命令的流
            byte[] b = new byte[1024];
            int size;
            StringBuffer sBuffer = new StringBuffer(300);
            pStream.println("\n"); //写命令
            while(true) {     //读取Server返回来的数据，直到读到登陆标识，这个时候认为可以输入用户名
                pStream.println("ls");
                size = inputStream.read(b);
                System.err.println(size);
                if(-1 != size) {
                    sBuffer.append(new String(b,0,size));
                    if(sBuffer.toString().trim().endsWith("dubbo>")) {
                        break;
                    }
                }
            }
            pStream.println("ls");
            System.out.println(sBuffer.toString());
            pStream.println("exit"); //写命令
            pStream.flush(); //将命令发送到telnet Server
            if(null != pStream) {
                pStream.close();
            }
            telnetClient.disconnect();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testTelnet() {

        try {
            System.out.println("启动Telnet...");
            String ip = "47.99.131.239";
            int port = 10008;
            String user = "admin";
            String password = "****";
            TelnetConnection telnet = new TelnetConnection(ip, port);
            telnet.setPrompt("<Quidway>");
          //  telnet.login(user, password, "");
            //telnet.setPrompt("\n");
            String r1 = telnet.sendCommand("ls");//display snmp-agent local-engineid
            System.out.println("显示结果");
            System.out.println(r1);
            telnet.setPrompt("[Quidway-Ethernet1/0/5]");
            String r2 = telnet.sendCommand("interface Ethernet1/0/5");
            String r3 = telnet.sendCommand("undo shutdown");
            System.out.println("显示结果");
            System.out.println(r1);
            System.out.println(r2);
            System.out.println(r3);

            System.out.println((r3.indexOf(telnet.getPrompt()) != -1) && r3.indexOf("^") == -1);
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}



