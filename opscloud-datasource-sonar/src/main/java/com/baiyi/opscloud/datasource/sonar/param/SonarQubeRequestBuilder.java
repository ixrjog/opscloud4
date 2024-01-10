package com.baiyi.opscloud.datasource.sonar.param;

import com.baiyi.opscloud.datasource.sonar.enums.QualifierEnum;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/3/15 10:45 上午
 * @Version 1.0
 */
public class SonarQubeRequestBuilder {

    private final SonarQubeParamMap request = new SonarQubeParamMap();

    private SonarQubeRequestBuilder() {
    }

    static public SonarQubeRequestBuilder newBuilder() {
        return new SonarQubeRequestBuilder();
    }

    public SonarQubeParamMap build() {
        return request;
    }

    public SonarQubeRequestBuilder putParam(QualifierEnum[] qualifiers) {
        request.putParam("qualifiers", Joiner.on(",").join(Arrays.stream(qualifiers).map(Enum::name).collect(Collectors.toList())));
        return this;
    }

    public SonarQubeRequestBuilder putParam(PagingParam pagingParam) {
        request.putParam(pagingParam.toParamMap());
        return this;
    }

    public SonarQubeRequestBuilder putParam(String key, String value) {
        if (!StringUtils.isBlank(value)) {
            request.putParam(key, value);
        }
        return this;
    }

}