package com.sdg.cmdb.domain.aliyun;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/12.
 */
@Data
public class AliyunEcsImageDO implements Serializable {
    private static final long serialVersionUID = 1412441161180428711L;

    private long id;
    private String imageId;
    private String imageDesc;
    private int imageType;
    private String version;
    private int size;
    private String gmtCreate;
    private String gmtModify;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public enum ImageTypeEnum {
        //0 保留
        def(0, "DEFAULT"),
        ecs(1, "ECS"),
        db(2, "DB"),
        redis(3, "REDIS");
        private int code;
        private String desc;

        ImageTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getImageTypeName(int code) {
            for (ImageTypeEnum imageTypeEnum : ImageTypeEnum.values()) {
                if (imageTypeEnum.getCode() == code) {
                    return imageTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

}
