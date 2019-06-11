package com.sdg.cmdb.util;



public class StringFilterUtils {

    /**
     * 过滤commit中的特殊字符（Markdown格式）
     * @param commit
     * @return
     */
    public static String commitFilter(String commit) {
        String cf= commit.replaceAll("\"", "");
        cf = cf.replaceAll("#","");
        return cf;
    }

}
