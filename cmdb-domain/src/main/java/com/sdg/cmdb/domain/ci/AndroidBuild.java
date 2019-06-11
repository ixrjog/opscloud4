package com.sdg.cmdb.domain.ci;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;

@Data
public class AndroidBuild implements Serializable {

    private static final long serialVersionUID = 8593826831110354084L;

    private String env;  // ENVIRONMENT_BUILD
    private String productFlavor; // PRODUCT_FLAVOR_BUILD

    /**
     * 处理参数
     * @param params
     */
    public void processor(HashMap<String, String> params) {
        if(!StringUtils.isEmpty(env))
            params.put("ENVIRONMENT_BUILD",env);
        if(!StringUtils.isEmpty(productFlavor))
            params.put("PRODUCT_FLAVOR_BUILD",productFlavor);

    }

}
