package com.sdg.cmdb.domain.product;

import com.sdg.cmdb.domain.auth.UserDO;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/2/20.
 */
public class ProductLifecycleManagementVO  implements Serializable {
    private static final long serialVersionUID = 2789686399817883921L;

    private long id;

    private String productName;

    private String content;

    private int productType;

    private long userId=0;

    private String username;

    private String mail;

    private long userId2=0;

    private String username2;

    private String mail2;

    private String startDate;

    private String endDate;

    private String gmtCreate;

    private String gmtModify;

    private boolean notice;

    public ProductLifecycleManagementVO(){

    }


    public ProductLifecycleManagementVO(ProductLifecycleManagementDO productLifecycleManagementDO){
        this.id=productLifecycleManagementDO.getId();
        this.productName=productLifecycleManagementDO.getProductName();
        this.content=productLifecycleManagementDO.getContent();
        this.productType=productLifecycleManagementDO.getProductType();
        this.startDate=productLifecycleManagementDO.getStartDate();
        this.endDate=productLifecycleManagementDO.getEndDate();
        this.gmtCreate=productLifecycleManagementDO.getGmtCreate();
        this.gmtModify=productLifecycleManagementDO.getGmtModify();
        this.notice=productLifecycleManagementDO.isNotice();
    }

    public ProductLifecycleManagementVO(ProductLifecycleManagementDO productLifecycleManagementDO,UserDO userDO){
        this.id=productLifecycleManagementDO.getId();
        this.productName=productLifecycleManagementDO.getProductName();
        this.content=productLifecycleManagementDO.getContent();
        this.productType=productLifecycleManagementDO.getProductType();
        this.startDate=productLifecycleManagementDO.getStartDate();
        this.endDate=productLifecycleManagementDO.getEndDate();
        this.gmtCreate=productLifecycleManagementDO.getGmtCreate();
        this.gmtModify=productLifecycleManagementDO.getGmtModify();
        this.notice=productLifecycleManagementDO.isNotice();
        this.userId=userDO.getId();
        this.username=userDO.getUsername();
        this.mail=userDO.getMail();
    }


    public ProductLifecycleManagementVO(ProductLifecycleManagementDO productLifecycleManagementDO, UserDO userDO,UserDO userDO2){
        this.id=productLifecycleManagementDO.getId();
        this.productName=productLifecycleManagementDO.getProductName();
        this.content=productLifecycleManagementDO.getContent();
        this.productType=productLifecycleManagementDO.getProductType();
        this.startDate=productLifecycleManagementDO.getStartDate();
        this.endDate=productLifecycleManagementDO.getEndDate();
        this.gmtCreate=productLifecycleManagementDO.getGmtCreate();
        this.gmtModify=productLifecycleManagementDO.getGmtModify();
        this.notice=productLifecycleManagementDO.isNotice();
        this.userId=userDO.getId();
        this.username=userDO.getUsername();
        this.mail=userDO.getMail();
        this.userId2=userDO2.getId();
        this.username2=userDO2.getUsername();
        this.mail2=userDO2.getMail();
    }

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
        return "ProductLifecycleManagementVO{" +
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
}
