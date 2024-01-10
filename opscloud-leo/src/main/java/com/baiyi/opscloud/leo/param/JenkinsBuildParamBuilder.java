package com.baiyi.opscloud.leo.param;

import com.baiyi.opscloud.leo.domain.param.JenkinsBuildParamMap;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/1/12 5:30 下午
 * @Version 1.0
 */
public class JenkinsBuildParamBuilder {

    private final JenkinsBuildParamMap paramMap = new JenkinsBuildParamMap();

    private JenkinsBuildParamBuilder() {
    }

    static public JenkinsBuildParamBuilder newBuilder() {
        return new JenkinsBuildParamBuilder();
    }

    public JenkinsBuildParamBuilder paramEntry(String paramName, Boolean value) {
        if (value != null) {
            paramMap.putParam(paramName, Boolean.toString(value));
        }
        return this;
    }

    public JenkinsBuildParamBuilder paramEntry(String paramName, String value) {
        if (!StringUtils.isEmpty(value)) {
            paramMap.putParam(paramName, value);
        }
        return this;
    }

    public JenkinsBuildParamBuilder paramEntries(Map<String, String> entries) {
        paramMap.putParams(entries);
        return this;
    }

    public JenkinsBuildParamMap build() {
        return paramMap;
    }

}