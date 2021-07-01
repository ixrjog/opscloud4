package com.baiyi.caesar.domain.vo.jenkins;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/3/17 4:01 下午
 * @Version 1.0
 */
public class JenkinsVO {

    public interface IJenkinsParameter {

        String getParameterYaml();

        void setParameters(Map<String, String> parameters);
    }

}
