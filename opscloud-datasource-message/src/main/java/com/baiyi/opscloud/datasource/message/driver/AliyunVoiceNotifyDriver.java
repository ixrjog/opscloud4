package com.baiyi.opscloud.datasource.message.driver;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.datasource.aliyun.core.SimpleAliyunClient;
import com.baiyi.opscloud.datasource.message.AliyunVoiceNotifyResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2022/7/22 10:47 PM
 * @Since 1.0
 */

@Component
@RequiredArgsConstructor
public class AliyunVoiceNotifyDriver {

    private final SimpleAliyunClient aliyunClient;

    // calld可以通过QueryCallDetailByCallId接口查询呼叫详情。
    public String singleCallByTts(String regionId, AliyunConfig.Aliyun aliyun, String phone, String ttsCode) {
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dyvmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SingleCallByTts");
        request.putQueryParameter("CalledShowNumber", "057156140102");
        request.putQueryParameter("CalledNumber", phone);
        request.putQueryParameter("TtsCode", ttsCode);
        IAcsClient iAcsClient = aliyunClient.buildAcsClient(regionId, aliyun);
        try {
            CommonResponse response = iAcsClient.getCommonResponse(request);
            if (response.getHttpStatus() == 200) {
                AliyunVoiceNotifyResponse.SingleCallByTts data =
                        JSONUtil.readValue(response.getData(), AliyunVoiceNotifyResponse.SingleCallByTts.class);
                return data.getCallId();
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

    public String queryCallDetailByCallId(String regionId, AliyunConfig.Aliyun aliyun, String callId, Long queryDate) {
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dyvmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("QueryCallDetailByCallId");
        request.putQueryParameter("ProdId", "11000000300006");
        request.putQueryParameter("CallId", callId);
        request.putQueryParameter("QueryDate", String.valueOf(queryDate));
        IAcsClient iAcsClient = aliyunClient.buildAcsClient(regionId, aliyun);
        try {
            CommonResponse response = iAcsClient.getCommonResponse(request);
            System.err.println(response);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }


}
