package com.sdg.cmdb.domain.product;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/2/20.
 */

/**
 * 产品周期管理
 */
public class ProductLifecycleManagementDO implements Serializable {


    private static final long serialVersionUID = -8049895923060613328L;

    private long id;

    private String productName;

    private String content;

    private int productType;

    private long userId;

    private long userId2;

    private String startDate;

    private String endDate;

    private String gmtCreate;

    private String gmtModify;

    private boolean notice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserId2() {
        return userId2;
    }

    public void setUserId2(long userId2) {
        this.userId2 = userId2;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public boolean isNotice() {
        return notice;
    }

    public void setNotice(boolean notice) {
        this.notice = notice;
    }

    @Override
    public String toString() {
        return "ProductLifecycleManagementDO{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", content='" + content + + '\'' +
                ", productType='" + productType +
                ", userId=" + userId +
                ", userId2=" + userId2 +
                ", notice=" + notice  +
                ", startDate='" + gmtCreate + '\'' +
                ", endDate='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

    public enum TypeEnum {
        //0 保留
        def(0, "系统默认"),
        sslCertificates(1, "SSL证书"),
        domain(2, "域名"),
        broadband(3, "宽带");

        private int code;
        private String desc;

        TypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getIpUseTypeName(int code) {
            for (TypeEnum typeEnum : TypeEnum.values()) {
                if (typeEnum.getCode() == code) {
                    return typeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

}
