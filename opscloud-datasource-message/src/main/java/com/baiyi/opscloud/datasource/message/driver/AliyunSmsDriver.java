package com.baiyi.opscloud.datasource.message.driver;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.datasource.aliyun.core.SimpleAliyunClient;
import com.baiyi.opscloud.datasource.message.AliyunSmsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author 修远
 * @Date 2022/8/1 5:24 PM
 * @Since 1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunSmsDriver {

    private final SimpleAliyunClient aliyunClient;

    private static final String SIGN_NAME = "PalmPay";
    private static final String DEFAULT_REGIN_ID = "cn-hangzhou";
    private static final String OK = "OK";

    /**
     * 根据该ID在接口QuerySendDetails中查询具体的发送状态
     * @param aliyun
     * @param phones
     * @param templateCode
     * @return
     */
    public String sendBatchSms(AliyunConfig.Aliyun aliyun, Set<String> phones, String templateCode) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendBatchSms");
        request.putQueryParameter("SignNameJson", JSONUtil.writeValueAsString(phones.stream().map(e -> SIGN_NAME).collect(Collectors.toList())));
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("PhoneNumberJson", JSONUtil.writeValueAsString(phones));
        IAcsClient iAcsClient = aliyunClient.buildAcsClient(DEFAULT_REGIN_ID, aliyun);
        try {
            CommonResponse response = iAcsClient.getCommonResponse(request);
            if (response.getHttpStatus() == 200) {
                AliyunSmsResponse.SendBatchSms data =
                        JSONUtil.readValue(response.getData(), AliyunSmsResponse.SendBatchSms.class);
                if (data != null && OK.equals(data.getCode())) {
                    return data.getBizId();
                }
                if (data != null) {
                    log.error("sendBatchSms失败: {}", data.getMessage());
                }
            }
        } catch (ClientException e) {
            log.error("sendBatchSms失败: {}", e.getMessage());
        }
        return StringUtils.EMPTY;
    }

}