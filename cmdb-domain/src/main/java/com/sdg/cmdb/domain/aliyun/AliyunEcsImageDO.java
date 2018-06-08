package com.sdg.cmdb.domain.aliyun;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/12.
 */
public class AliyunEcsImageDO implements Serializable {
    private static final long serialVersionUID = 1412441161180428711L;

    private long id;

    private String imageId;

    private String imageDesc;

    private int imageType;

    private String version;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageDesc() {
        return imageDesc;
    }

    public void setImageDesc(String imageDesc) {
        this.imageDesc = imageDesc;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    @Override
    public String toString() {
        return "AliyunEcsImageDO{" +
                "id=" + id +
                ", imageId='" + imageId + '\'' +
                ", imageDesc='" + imageDesc + '\'' +
                ", imageType=" + imageType +
                ", version='" + version + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
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
