package com.sdg.cmdb.domain.todo;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/10/11.
 */
public class TodoDailyDO implements Serializable {
    private static final long serialVersionUID = 5427411491545874232L;

    private long id;

    /*
    一级类目
     */
    private long levelOne;

    /*
    二级类目
     */
    private long levelTwo;

    /*
    工单发起人
     */
    private String sponsor;

    /*
    0：公开；1：私密
     */
    private int privacy;


    /*
    紧急程度。0：一般；1：重要；2：紧急
     */
    private int urgents;

    /*
    工单内容
     */
    private String contents;

    /*
    反馈内容
     */
    private String feedbackContent;

    /*
    工单状态。0：处理中；1：完成；2：待反馈
     */
    private int todoStatus;

    /*
    是否确认。0：未确认；1：发起人确认；2：系统确认。
     */
    private int hasConfirm;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(long levelOne) {
        this.levelOne = levelOne;
    }

    public long getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(long levelTwo) {
        this.levelTwo = levelTwo;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public int getUrgents() {
        return urgents;
    }

    public void setUrgents(int urgents) {
        this.urgents = urgents;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public int getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(int todoStatus) {
        this.todoStatus = todoStatus;
    }

    public int getHasConfirm() {
        return hasConfirm;
    }

    public void setHasConfirm(int hasConfirm) {
        this.hasConfirm = hasConfirm;
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
        return "TodoDailyDO{" +
                "id=" + id +
                ", levelOne=" + levelOne +
                ", levelTwo=" + levelTwo +
                ", sponsor='" + sponsor + '\'' +
                ", privacy=" + privacy +
                ", urgents=" + urgents +
                ", contents='" + contents + '\'' +
                ", feedbackContent='" + feedbackContent + '\'' +
                ", todoStatus=" + todoStatus +
                ", hasConfirm=" + hasConfirm +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

    public enum TodoPrivacyEnum {
        publicPrivacy(0, "公开"),
        internalPrivacy(1, "私密");
        int code;
        String desc;

        TodoPrivacyEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        /**
         * 获取指定code对应的描述
         * @param code
         * @return
         */
        public static String getCodeDesc(int code) {
            for(TodoPrivacyEnum privacyEnum : TodoPrivacyEnum.values()) {
                if (privacyEnum.getCode() == code) {
                    return privacyEnum.getDesc();
                }
            }
            return "未知";
        }
    }

    public enum TodoUrgentEnum {
        generalUrgent(0, "一般"),
        importantUrgent(1, "重要"),
        UrgentUrent(2, "紧急");

        int code;
        String desc;

        TodoUrgentEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getCodeDesc(int code) {
            for(TodoUrgentEnum urgentEnum : TodoUrgentEnum.values()) {
                if (urgentEnum.getCode() == code) {
                    return urgentEnum.getDesc();
                }
            }
            return "未知";
        }
    }

    public enum TodoStatusEnum {
        processStatus(0, "处理中"),
        finishStatus(1, "完成"),
        feedbackStatus(2, "待反馈");
        int code;
        String desc;

        TodoStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getCodeDesc(int code) {
            for(TodoStatusEnum statusEnum : TodoStatusEnum.values()) {
                if (statusEnum.getCode() == code) {
                    return statusEnum.getDesc();
                }
            }
            return "未知";
        }
    }

    public enum TodoConfirmEnum {
        notConfirm(0, "未确认"),
        sendConfirm(1, "发起人确认"),
        systemConfirm(2, "系统确认");
        int code;
        String desc;

        TodoConfirmEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getCodeDesc(int code) {
            for(TodoConfirmEnum confirmEnum : TodoConfirmEnum.values()) {
                if (confirmEnum.getCode() == code) {
                    return confirmEnum.getDesc();
                }
            }
            return "未知";
        }
    }
}
