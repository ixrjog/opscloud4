package com.sdg.cmdb.util;

import org.apache.commons.lang.StringUtils;

public class PathUtils {

    /**
     * 取组合路径
     * @param p
     * @return
     */
    public static String getPath(String... p) {
        String path = "";
        for (String x : p) {
            if (StringUtils.isEmpty(path)) {
                path = x;
            } else {
                path += "/" + x;
            }
        }
        path = path.replaceAll("///", "/");
        path = path.replaceAll("//", "/");
        return path;
    }
}
