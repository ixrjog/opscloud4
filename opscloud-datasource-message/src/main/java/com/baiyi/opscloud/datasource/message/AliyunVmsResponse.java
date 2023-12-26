package com.baiyi.opscloud.datasource.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author 修远
 * @Date 2022/7/23 12:16 AM
 * @Since 1.0
 */
public class AliyunVmsResponse {

    @Data
    public static class SingleCallByTts {

        @JsonProperty("Message")
        private String message;
        @JsonProperty("RequestId")
        private String requestId;
        @JsonProperty("CallId")
        private String callId;
        @JsonProperty("Code")
        private String code;

    }

    @Data
    public static class QueryCallDetailByCallId {

        @JsonProperty("Message")
        private String message;
        @JsonProperty("RequestId")
        private String requestId;
        @JsonProperty("Data")
        private String data;
        @JsonProperty("Code")
        private String code;

    }

    /**
     * caller：主叫号码。
     * startDate：通话开始时间。
     * stateDesc：通话状态描述。
     * duration：通话时长。单位：秒。0：用户未接通。
     * callerShowNumber：主叫显号。
     * gmtCreate：通话请求的接收时间。
     * state：通话状态。通话状态是运营商实时返回的，更多状态信息，请参见运营商回执错误码。
     * endDate：通话结束时间。
     * calleeShowNumber：被叫显号。
     * callee：被叫号码。
     * aRingTime：a路响铃开始时间。格式：yyyy-MM-dd HH:mm:ss。
     * aEndTime：a路响铃结束时间。格式：yyyy-MM-dd HH:mm:ss。
     * bRingTime：b路响铃开始时间。格式：yyyy-MM-dd HH:mm:ss。
     * bEndTime：b路响铃结束时间。格式：yyyy-MM-dd HH:mm:ss。
     */

    @Data
    public static class CallDetail {

        private String callId;
        private String endDate;
        private String stateDesc;
        private String callee;
        private String gmtCreate;
        private Integer duration;
        private String calleeShowNumber;
        private String bStartTime;
        private String bRingTime;
        private String state;
        private String startDate;
        private Integer hangupDirection;

    }

}