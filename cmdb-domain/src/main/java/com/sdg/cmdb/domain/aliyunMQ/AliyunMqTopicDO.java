package com.sdg.cmdb.domain.aliyunMQ;

import com.sdg.cmdb.domain.auth.UserDO;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
public class AliyunMqTopicDO implements Serializable {
    private static final long serialVersionUID = -3308135003436455747L;

    private long id;
    private String topic;
    private String remark;
    private long userId;
    private int messageType;
    private String displayName;
    private String gmtCreate;
    private String gmtModify;

    public enum MessageTypeEnum {
        //0 保留／在组中代表的是所有权限
        ordinary(0, "普通消息"),
        partitionOrder(1, "分区顺序消息"),
        publicOrder(2, "全局顺序消息"),
        transaction(4, "事务消息"),
        timingOrDelay(5, "定时/延时消息");
        private int code;
        private String desc;

        MessageTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getMessageTypeName(int code) {
            for (MessageTypeEnum messageTypeEnum : MessageTypeEnum.values()) {
                if (messageTypeEnum.getCode() == code) {
                    return messageTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    public AliyunMqTopicDO(String topic, String remark, UserDO userDO) {
        this.topic = topic;
        this.remark = remark;
        this.userId = userDO.getId();
        if (StringUtils.isEmpty(userDO.getDisplayName())) {
            this.displayName = userDO.getUsername();
        } else {
            this.displayName = userDO.getUsername() + "<" + userDO.getDisplayName() + ">";
        }
    }

    public AliyunMqTopicDO() {

    }

}
