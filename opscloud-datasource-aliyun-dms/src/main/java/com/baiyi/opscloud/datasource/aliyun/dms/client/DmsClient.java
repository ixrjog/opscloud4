package com.baiyi.opscloud.datasource.aliyun.dms.client;

import com.aliyun.dms_enterprise20181101.models.RegisterUserRequest;
import com.aliyun.dms_enterprise20181101.models.RegisterUserResponse;
import com.aliyun.tea.TeaConverter;
import com.aliyun.tea.TeaModel;
import com.aliyun.tea.TeaPair;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teaopenapi.models.OpenApiRequest;
import com.aliyun.teaopenapi.models.Params;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/12/17 11:46 AM
 * @Version 1.0
 */
public class DmsClient extends com.aliyun.dms_enterprise20181101.Client {

    public DmsClient(Config config) throws Exception {
        super(config);
    }

    @Override
    public RegisterUserResponse registerUserWithOptions(RegisterUserRequest request, RuntimeOptions runtime) throws Exception {
        Common.validateModel(request);
        Map<String, Object> query = Maps.newHashMap();
        query.put("Mobile", request.mobile);
        query.put("RoleNames", request.roleNames);
        query.put("Tid", request.tid);
        query.put("Uid", request.uid);
        query.put("UserNick", request.userNick);
        query.put("notificationMode", "DINGDING");
        OpenApiRequest req = OpenApiRequest.build(TeaConverter.buildMap(new TeaPair[]{new TeaPair("query", com.aliyun.openapiutil.Client.query(query)), new TeaPair("body", Common.toMap(request))}));
        Params params = Params.build(TeaConverter.buildMap(
                new TeaPair[]{new TeaPair("action", "RegisterUser"),
                        new TeaPair("version", "2018-11-01"),
                        new TeaPair("protocol", "HTTPS"),
                        new TeaPair("pathname", "/"),
                        new TeaPair("method", "POST"),
                        new TeaPair("authType", "AK"),
                        new TeaPair("style", "RPC"),
                        new TeaPair("reqBodyType", "json"),
                        new TeaPair("bodyType", "json")}));
        return TeaModel.toModel(this.callApi(params, req, runtime), new RegisterUserResponse());
    }

}