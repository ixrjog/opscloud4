package com.sdg.cmdb.domain.dubbo;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Dubbod 生产者信息
 */
@Data
public class DubboProvider implements Serializable {

    private String ip;
    private String dubboInterface;
    private String dubboPort;

    public DubboProvider(){

    }

    public DubboProvider(String dubboInfo){
        String url = URLDecoder.decode(dubboInfo);
        url = url.replace("dubbo://","http://");
        try{
            URL u = new URL(url);
            this.ip = u.getHost();
            this.dubboPort = String.valueOf(u.getPort())  ;
            this.dubboInterface = u.getPath().substring(1,u.getPath().length());
        }catch (Exception e){

        }




        /**
         * dubbo%3A%2F%2F
         * 172.20.22.25%3A20880
         * %2F
         * com.ggj.platform.promotion.api.coupon.CouponAPI
         * %3F
         * anyhost
         */
//        String info = dubboInfo.replace("dubbo%3A%2F%2F", "");
//        String[] s1 = info.split("%2F");
//        String[] s2 = s1[0].split("%3A");
//        this.ip = s2[0];
//        this.dubboPort = s2[1];
//        String[] s3 = s1[1].split("%3F");
//        this.dubboInterface = s3[0];
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
