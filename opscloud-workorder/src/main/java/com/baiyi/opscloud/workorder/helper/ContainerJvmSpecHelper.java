package com.baiyi.opscloud.workorder.helper;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2023/11/2 11:16
 * @Version 1.0
 */
public class ContainerJvmSpecHelper {

    static final String SMALL = "-Xms1G -Xmx1G -Xmn500M -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=128M -XX:MaxDirectMemorySize=256M -XX:ReservedCodeCacheSize=128M";
    static final String LARGE = "-Xms2560M -Xmx2560M -Xmn1200M -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=256M -XX:MaxDirectMemorySize=256M -XX:ReservedCodeCacheSize=128M";
    static final String XLARGE = "-Xms4G -Xmx4G -Xmn2G -XX:MetaspaceSize=384M -XX:MaxMetaspaceSize=384M -XX:MaxDirectMemorySize=1G -XX:ReservedCodeCacheSize=256M";
    static final String XLARGE2 = "-Xms10G -Xmx10G -Xmn5G -XX:MetaspaceSize=512M -XX:MaxMetaspaceSize=512M -XX:MaxDirectMemorySize=1G -XX:ReservedCodeCacheSize=256M";

    private static final String[] JVM_ARGS = {"-Xms", "-Xmx", "-Xmn", "-XX:MetaspaceSize", "-XX:MaxMetaspaceSize", "-XX:MaxDirectMemorySize", "-XX:ReservedCodeCacheSize"};

    public static final Map<String, String> JVMContainerSpecMap = buildMap();

    private static Map<String, String> buildMap() {
        Map<String, String> jvmContainerSpecMap = Maps.newHashMap();
        jvmContainerSpecMap.put("SMALL", SMALL);
        jvmContainerSpecMap.put("LARGE", LARGE);
        jvmContainerSpecMap.put("XLARGE", XLARGE);
        jvmContainerSpecMap.put("XLARGE2", XLARGE2);
        return jvmContainerSpecMap;
    }

    public static List<String> parse(String spec, String javaOpts) {
        // 当前JAVA_OPTS
        List<String> originalArgList = Splitter.onPattern(" |\\n").omitEmptyStrings().splitToList(javaOpts);
        // 新Args结果集
        List<String> result = Lists.newArrayList(JVMContainerSpecMap.get(spec));
        // 查找没有的参数附加到新Args结果集
        originalArgList.stream().filter(originalArg -> Arrays.stream(JVM_ARGS).noneMatch(originalArg::startsWith)).forEach(result::add);
        return result;
    }

    public static String format(List<String> jvmArgs) {
        return Joiner.on(" ").skipNulls().join(jvmArgs);
    }

}