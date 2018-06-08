package com.sdg.cmdb.domain.dns;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/7/11.
 */
public class DnsmasqDO implements Serializable {
    private static final long serialVersionUID = 7562425153421096263L;

    private long id;

    private long dnsGroupId;

    private int itemType;

    private String item;

    private String itemValue;

    private String content;

    private String gmtCreate;

    private String gmtModify;

    @Override
    public String toString() {
        return "DnsmasqDO{" +
                "id=" + id +
                ", dnsGroupId=" + dnsGroupId +
                ", itemType=" + itemType +
                ", item='" + item + '\'' +
                ", itemValue='" + itemValue + '\'' +
                ", content='" + content + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }


    public enum ItemTypeEnum {
        system(0, "system"),
        server(1, "server"),
        address(2, "address");
        private int code;
        private String desc;
        ItemTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getItemTypeName(int code) {
            for (ItemTypeEnum itemTypeEnum : ItemTypeEnum.values()) {
                if (itemTypeEnum.getCode() == code) {
                    return itemTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDnsGroupId() {
        return dnsGroupId;
    }

    public void setDnsGroupId(long dnsGroupId) {
        this.dnsGroupId = dnsGroupId;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
