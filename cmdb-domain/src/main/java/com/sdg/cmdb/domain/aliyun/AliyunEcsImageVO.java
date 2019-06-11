package com.sdg.cmdb.domain.aliyun;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/12.
 */
@Data
public class AliyunEcsImageVO implements Serializable {

    private static final long serialVersionUID = 7029369118678262545L;

    private long id;
    private String imageName;
    private int imageType;
    private int size;
    private String gmtCreate;
    private String gmtModify;

    public AliyunEcsImageVO(){}

    public AliyunEcsImageVO(AliyunEcsImageDO aliyunEcsImageDO) {
        this.id = aliyunEcsImageDO.getId();
        this.imageType = aliyunEcsImageDO.getImageType();
        this.size = aliyunEcsImageDO.getSize();
        this.gmtCreate = aliyunEcsImageDO.getGmtCreate();
        this.gmtModify = aliyunEcsImageDO.getGmtModify();
        this.imageName = aliyunEcsImageDO.getImageDesc() + "(" + AliyunEcsImageDO.ImageTypeEnum.getImageTypeName(aliyunEcsImageDO.getImageType()) +","+size+ "G)" + aliyunEcsImageDO.getVersion();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
