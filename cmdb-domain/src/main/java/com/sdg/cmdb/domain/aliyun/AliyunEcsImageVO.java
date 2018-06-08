package com.sdg.cmdb.domain.aliyun;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/12.
 */
public class AliyunEcsImageVO implements Serializable {

    private static final long serialVersionUID = 7029369118678262545L;

    private long id;

    private String imageName;

    private int imageType;

    private String gmtCreate;

    private String gmtModify;

    public AliyunEcsImageVO(){}

    public AliyunEcsImageVO(AliyunEcsImageDO aliyunEcsImageDO) {
        this.id = aliyunEcsImageDO.getId();
        this.imageType = aliyunEcsImageDO.getImageType();
        this.gmtCreate = aliyunEcsImageDO.getGmtCreate();
        this.gmtModify = aliyunEcsImageDO.getGmtModify();
        this.imageName = aliyunEcsImageDO.getImageDesc() + "(" + AliyunEcsImageDO.ImageTypeEnum.getImageTypeName(aliyunEcsImageDO.getImageType()) + ")" + aliyunEcsImageDO.getVersion();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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
        return "AliyunEcsImageVO{" +
                "id=" + id +
                ", imageType=" + imageType +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }

}
