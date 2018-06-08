package com.sdg.cmdb.extend;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxxiao on 2017/5/15.
 */
public class InvokeInvocation implements Invocation, Serializable {

    private static final long serialVersionUID = -5715201947596971943L;

    private Map<String, Object> parameterMap = new HashMap<>();

    public InvokeInvocation(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    @Override
    public String getParameterForString(String key) {
        return parameterMap.get(key).toString();
    }
}
